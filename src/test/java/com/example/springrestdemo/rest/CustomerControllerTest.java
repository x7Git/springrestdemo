package com.example.springrestdemo.rest;

import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.entity.enumeration.RoleType;
import com.example.springrestdemo.service.CustomerService;
import com.example.springrestdemo.service.DTO.CustomerDTO;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CustomerControllerTest {

    @Mock
    private CustomerService mockCustomerService;

    @InjectMocks
    private CustomerController classUnderTest;

    private static final long CUSTOMER_ID= 3489432L;
    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp(){
        customer = new Customer("username" , "Lokesh", "Gupta", "password", RoleType.CUSTOMER);
        customerDTO = new CustomerDTO();
    }

    @Test
    void getCustomers_findAllCustomers_ok() {
        //Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(mockCustomerService.getCustomers()).thenReturn(Collections.singletonList(customer));
        //Act
        ResponseEntity<List<Customer>> responseEntity = classUnderTest.getCustomers();
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotEmpty();
        assertThat(responseEntity.getBody().get(0)).isEqualTo(customer);
    }

    @Test
    void getCustomer_costumerFound_okay() {
        //Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(mockCustomerService.getCustomerById(anyLong())).thenReturn(customer);
        //Act
        ResponseEntity<Customer> responseEntity = classUnderTest.getCustomer(234L);
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(customer);
    }

    @Test
    void putCustomer_customerUpdated_ok() {
            //Arrange
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
            //Act
            ResponseEntity<String> responseEntity = classUnderTest.putCustomer(customerDTO);
            //Assert
            assertThat(responseEntity.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    void deleteCustomer_customerDeleted_ok() {
        //Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        //Act
        ResponseEntity<String> responseEntity = classUnderTest.deleteCustomer(234L);
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(204);
    }
}