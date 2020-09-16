package com.example.springrestdemo.service;

import com.example.springrestdemo.authentication.JwtTokenUtil;
import com.example.springrestdemo.db.entity.Contract;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.entity.enumeration.ContractType;
import com.example.springrestdemo.db.repository.ContractRepository;
import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ContractServiceTest {
    @Mock
    private ContractRepository mockContractRepository;
    @Mock
    private CustomerRepository mockCustomerRepository;
    private static final String JWT_TOKEN_BEARER = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImV4cCI6MTU5MjE1MTg3OSwiaWF0IjoxNTkyMTMzODc5fQ._pdSAQ1to8m181swsja4tF7bB-zteJzxx3gQMaJx5jbRzVcHo7hWrLgQlixh_yOyiLZ-Z7JcFCvINVkAbUGr6Q";

    @InjectMocks
    private ContractService classUnderTest;
    private static final String USERNAME = "username";
    private static final long CUSTOMER_ID = 3489432L;
    private static final long PRICE = 1599L;
    @Mock
    private JwtTokenUtil mockJwtTokenUtil;
    private Contract contract;
    private Customer customer;

    @BeforeEach
    void setUp() {
        contract = new Contract(ContractType.DSL, PRICE);
        customer = new Customer();
    }

    @Ignore
    @Test
    void addContract_ContractAdded_ok() {
        //Arrange
        when(mockCustomerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        //Act
        Contract result = classUnderTest.addContract(contract, CUSTOMER_ID);
        //Assert
        assertThat(result.getContractId()).isEqualTo(contract.getCustomer().getCustomerId());
    }

    @Test
    void deleteCustomer_ContractDeleted_ok() {
        //Act
        classUnderTest.deleteContract(45L);
        //Assert
        verify(mockContractRepository).deleteById(45L);
    }

    @Test
    void calculatePrice_noContracts_ok() {
        //Arrange
        when(mockJwtTokenUtil.getUsernameFromToken(anyString())).thenReturn(USERNAME);
        when(mockCustomerRepository.findByUsername(USERNAME)).thenReturn(Optional.of(customer));
        //Act
        long calculatePrice = classUnderTest.calculatePrice(JWT_TOKEN_BEARER);
        //Assert
        assertThat(calculatePrice).isEqualTo(0L);
    }


    @Test
    void calculatePrice_twoContracts_ok() {
        //Arrange
        when(mockJwtTokenUtil.getUsernameFromToken(anyString())).thenReturn(USERNAME);
        when(mockCustomerRepository.findByUsername(USERNAME)).thenReturn(Optional.of(customer));
        List<Contract> contracts = new ArrayList<>();
        contracts.add(new Contract(ContractType.DSL, 799L));
        contracts.add(new Contract(ContractType.DSL, 1599L));
        customer.setContracts(contracts);
        //Act
        long calculatePrice = classUnderTest.calculatePrice(JWT_TOKEN_BEARER);
        //Assert
        assertThat(calculatePrice).isEqualTo(799L + 1599L);
    }

    @Test
    void findCustomer_noCustomerFound_throwNoEntityFoundException(){
        assertThrows(NoEntityFoundException.class, () -> classUnderTest.findCustomer(CUSTOMER_ID));
    }

    @Test
    void findCustomer_twoContracts_ok() {
        //Arrange
        when(mockCustomerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        //Act
        Customer customer = classUnderTest.findCustomer(CUSTOMER_ID);
        //Assert
        assertThat(customer).isEqualTo(this.customer);
    }
}