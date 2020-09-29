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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CustomerServiceTest {
    private static final long CUSTOMER_ID = 3489432L;
    @Mock
    private CustomerRepository mockCustomerRepository;
    @Mock
    private PasswordEncoder mockArgon2Encoder;
    @Mock
    private CustomerDetailsService customerDetailsService;
    @Mock
    private AuthenticationManager mockAuthenticationManager;
    @Mock
    private JwtTokenUtil mockJwtTokenUtil;
    @InjectMocks
    private CustomerService classUnderTest;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("username", "Lokesh", "Gupta", "password", RoleType.SERVICE);
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

    @Test
    void addCustomer_newCustomerSaved_ok() {
        //Arrange
        String argon2Hashed = "$argon2id$v=19$m=12,t=3,p=1$MWhOdnI1QVVuaUpEWVRtUw$tvD5BxSHb+YmosR4nzdvhg";
        when(mockArgon2Encoder.encode("password")).thenReturn(argon2Hashed);
        when(mockCustomerRepository.save(customer)).thenReturn(customer);
        //Act
        Customer result = classUnderTest.addCustomer(customer);
        //Assert
        assertThat(result.getPassword()).isEqualTo(argon2Hashed);
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
        when(customerDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));
        when(mockJwtTokenUtil.generateToken(customerDetailsService.loadUserByUsername("username"))).thenReturn(jwtToken);
        //Act
        JwtResponse result = classUnderTest.authenticate(jwtRequest);
        //Assert
        assertThat(result.getToken()).isEqualTo(jwtToken);
        verify(mockAuthenticationManager).authenticate(any());
    }

}