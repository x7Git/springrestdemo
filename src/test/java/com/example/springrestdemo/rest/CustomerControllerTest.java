package com.example.springrestdemo.rest;

import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.entity.enumeration.RoleType;
import com.example.springrestdemo.service.CustomerService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CustomerControllerTest {
    private static final String JWT_TOKEN_BEARER = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImV4cCI6MTU5MjE1MTg3OSwiaWF0IjoxNTkyMTMzODc5fQ._pdSAQ1to8m181swsja4tF7bB-zteJzxx3gQMaJx5jbRzVcHo7hWrLgQlixh_yOyiLZ-Z7JcFCvINVkAbUGr6Q";
    private static final long CUSTOMER_ID = 3489432L;
    @Mock
    private CustomerService mockCustomerService;
    @InjectMocks
    private CustomerController classUnderTest;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("username", "Lokesh", "Gupta", "password", RoleType.CUSTOMER);
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
        when(mockCustomerService.getCustomerByName(anyString())).thenReturn(customer);
        //Act
        ResponseEntity<Customer> responseEntity = classUnderTest.getCustomer(JWT_TOKEN_BEARER);
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
        ResponseEntity<String> responseEntity = classUnderTest.putCustomer(customer);
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