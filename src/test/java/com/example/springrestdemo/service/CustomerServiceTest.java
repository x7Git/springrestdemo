package com.example.springrestdemo.service;

import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.entity.enumeration.RoleType;
import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository mockCustomerRepository;

    @InjectMocks
    private CustomerService classUnderTest;
    
    private static final long CUSTOMER_ID = 3489432L;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("username" , "Lokesh", "Gupta", "password", RoleType.SERVICE);
    }

    @Test
    public void getCustomers() {
        //Arrange
        when(mockCustomerRepository.findAll()).thenReturn(Collections.singletonList(new Customer()));
        //Act
        List<Customer> result = classUnderTest.getCustomers();
        //Assert
        assertThat(result).isNotEmpty();
    }

    @Test
    public void getCustomerById_NoCustomerFound_throwsNoCustomerFoundException() {
        assertThrows(NoEntityFoundException.class, () -> classUnderTest.getCustomerById(CUSTOMER_ID));
    }

    @Test
    public void getCustomerById_CustomerFound_ok() {
        //Arrange
        Customer customer = new Customer();
        when(mockCustomerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        //Act
        Customer result = classUnderTest.getCustomerById(CUSTOMER_ID);
        //Assert
        assertEquals(customer, result);
    }

    @Test
    void deleteCustomer_CustomerDeleted_ok() {
        //Act
        classUnderTest.deleteCustomer(CUSTOMER_ID);
        //Assert
        verify(mockCustomerRepository).deleteById(CUSTOMER_ID);
    }

    @Test
    void updateCustomer_NoCustomerFound_throwsNoCustomerFoundException() {
        assertThrows(NoEntityFoundException.class, () -> classUnderTest.updateCustomer(new Customer()));
    }

    @Test
    void updateCustomer_CustomerFound_ok() {
        //Arrange
        when(mockCustomerRepository.findByUsername(anyString())).thenReturn(Optional.of(customer));
        customer.setName("Flip");
        customer.setLastName("Natz");
        //Act
        classUnderTest.updateCustomer(customer);
        //Assert
        verify(mockCustomerRepository).save(customer);
    }
    @Test
    void updateCustomer_NameAndLastNameNULL_ok() {
        //Arrange
        when(mockCustomerRepository.findByUsername(anyString())).thenReturn(Optional.of(customer));
        customer.setName(null);
        customer.setLastName(null);
        //Act
        classUnderTest.updateCustomer(customer);
        //Assert
        verify(mockCustomerRepository).save(customer);
    }
}