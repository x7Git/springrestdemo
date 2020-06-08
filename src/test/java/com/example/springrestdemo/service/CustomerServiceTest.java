package com.example.springrestdemo.service;

import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import com.example.springrestdemo.rest.CustomerController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository mockCustomerRepository;

    @InjectMocks
    private CustomerService classUnderTest;
    
    private static final long CUSTOMERID= 3489432L;
    @Test
    void addCustomer() {
        Customer expected = new Customer();
        long result = classUnderTest.addCustomer(expected);
        assertThat(result).isEqualTo(expected.getCustomerId());
    }

    @Test
    public void getCustomers() {
        when(mockCustomerRepository.findAll()).thenReturn(Collections.singletonList(new Customer()));
        List<Customer> result = classUnderTest.getCustomers();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void getCustomerById_NoCustomerFound_throwsNoCustomerFoundException() {
        assertThrows(NoEntityFoundException.class, () -> classUnderTest.getCustomerById(CUSTOMERID));
    }

    @Test
    public void getCustomerById_CustomerFound_ok() {
        Customer expected = new Customer();
        when(mockCustomerRepository.findById(CUSTOMERID)).thenReturn(Optional.of(expected));
        Customer result = classUnderTest.getCustomerById(CUSTOMERID);
        assertEquals(expected, result);
    }

    @Test
    void deleteCustomer() {
        classUnderTest.deleteCustomer(CUSTOMERID);
        verify(mockCustomerRepository).deleteById(CUSTOMERID);
    }

    @Test
    void updateCustomer_NoCustomerFound_throwsNoCustomerFoundException() {
        assertThrows(NoEntityFoundException.class, () -> classUnderTest.updateCustomer(new Customer()));
    }
}