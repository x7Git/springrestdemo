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
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static com.example.springrestdemo.TestValues.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CustomerDetailsServiceTest {
    @Mock
    private CustomerRepository mockCustomerRepository;

    @InjectMocks
    private CustomerDetailsService classUnderTest;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(USERNAME, "Lokesh", "Gupta", "password", RoleType.SERVICE);
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
}