package com.example.springrestdemo.service;

import com.example.springrestdemo.authentication.JwtRequest;
import com.example.springrestdemo.authentication.JwtResponse;
import com.example.springrestdemo.authentication.JwtTokenUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CustomerDetailsServiceTest {
    @Mock
    private CustomerRepository mockCustomerRepository;

    @Mock
    private PasswordEncoder mockBcryptEncoder;

    @Mock
    private AuthenticationManager mockAuthenticationManager;

    @Mock
    private JwtTokenUtil mockJwtTokenUtil;

    @InjectMocks
    private CustomerDetailsService classUnderTest;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("username" , "Lokesh", "Gupta", "password", RoleType.SERVICE);
    }

    @Test
    void loadUserByUsername_noCustomerFoundByUsername_throwsNoEntityFoundException() {
        assertThrows(NoEntityFoundException.class, () -> classUnderTest.loadUserByUsername(anyString()));
    }

    @Test
    void loadUserByUsername_withRole_ok() {
        //Arrange
        when(mockCustomerRepository.findByUsername(anyString())).thenReturn(Optional.of(customer));
        //Act
        UserDetails userDetails = classUnderTest.loadUserByUsername(anyString());
        //Assert
        assertThat(userDetails.getUsername()).isEqualTo(customer.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(customer.getPassword());
        assertThat(userDetails.getAuthorities()).isNotEmpty();
    }

    @Test
    void loadUserByUsername_withoutRole_ok() {
        //Arrange
        customer.setRole(null);
        when(mockCustomerRepository.findByUsername(anyString())).thenReturn(Optional.of(customer));
        //Act
        UserDetails userDetails = classUnderTest.loadUserByUsername(anyString());
        //Assert
        assertThat(userDetails.getUsername()).isEqualTo(customer.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(customer.getPassword());
        assertThat(userDetails.getAuthorities()).isEmpty();
    }

    @Test
    void addCustomer_newCustomerSaved_ok() {
        //Arrange
        String bcryptHashed = "$2y$12$/WZfaY2zFl6ikd0hJ/PI8OOkj1yj9k0KC26mkZPfUk0K6ZTbofrUm";
        when(mockBcryptEncoder.encode("password")).thenReturn(bcryptHashed);
        when(mockCustomerRepository.save(customer)).thenReturn(customer);
        //Act
        Customer result = classUnderTest.addCustomer(customer);
        //Assert
        assertThat(result.getPassword()).isEqualTo(bcryptHashed);
        assertThat(result.getUsername()).isEqualTo(customer.getUsername());
        assertThat(result.getName()).isEqualTo(customer.getName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getRole()).isEqualTo(customer.getRole());
    }

    @Test
    void authenticate_jwtTokenGenerated_ok() {
        //Arrange
        JwtRequest jwtRequest = new JwtRequest("username", "password");
        String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImV4cCI6MTU5MjE1MTg3OSwiaWF0IjoxNTkyMTMzODc5fQ._pdSAQ1to8m181swsja4tF7bB-zteJzxx3gQMaJx5jbRzVcHo7hWrLgQlixh_yOyiLZ-Z7JcFCvINVkAbUGr6Q";
        when(mockCustomerRepository.findByUsername(anyString())).thenReturn(Optional.of(customer));
        UserDetails userDetails = classUnderTest.loadUserByUsername("username");
        when(mockJwtTokenUtil.generateToken(userDetails)).thenReturn(jwtToken);
        //Act
        JwtResponse result = classUnderTest.authenticate(jwtRequest);
        //Assert
        assertThat(result.getToken()).isEqualTo(jwtToken);
        verify(mockAuthenticationManager).authenticate(any());
    }
}