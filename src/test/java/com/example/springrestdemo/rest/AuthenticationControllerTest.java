package com.example.springrestdemo.rest;

import com.example.springrestdemo.authentication.JwtRequest;
import com.example.springrestdemo.authentication.JwtResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class AuthenticationControllerTest {

    @Mock
    private CustomerService mockCustomerService;

    @InjectMocks
    private AuthenticationController classUnderTest;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("username", "Lokesh", "Gupta", "password", RoleType.CUSTOMER);
    }

    @Test
    void createAuthenticationToken_generateToken_ok() {
        //Arrange
        JwtRequest jwtRequest = new JwtRequest("username", "password");
        JwtResponse jwtResponse = new JwtResponse("Bearer TOKEN");
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(mockCustomerService.authenticate(jwtRequest)).thenReturn(jwtResponse);
        //Act
        ResponseEntity<JwtResponse> responseEntity = classUnderTest.createAuthenticationToken(jwtRequest);
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    void postCustomer() {
        //Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(mockCustomerService.addCustomer(customer)).thenReturn(customer);
        //Act
        ResponseEntity<?> responseEntity = classUnderTest.postCustomer(customer);
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().get("Location")).isNotNull();
    }
}