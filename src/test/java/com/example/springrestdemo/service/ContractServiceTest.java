package com.example.springrestdemo.service;

import com.example.springrestdemo.db.entity.Contract;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.entity.enumeration.ContractType;
import com.example.springrestdemo.db.repository.ContractRepository;
import com.example.springrestdemo.db.repository.CustomerRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ContractServiceTest {
    @Mock
    private ContractRepository mockContractRepository;
    @Mock
    private CustomerRepository mockCustomerRepository;

    @InjectMocks
    private ContractService classUnderTest;
    
    private static final long CUSTOMER_ID= 3489432L;
    private Contract contract;
    private Customer customer;

    @BeforeEach
    void setUp() {
        contract = new Contract(ContractType.DSL);
        customer = new Customer();
    }
    @Ignore
    @Test
    void addContract_ContractAdded_ok() {
        when(mockCustomerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        long result = classUnderTest.addContract(contract, CUSTOMER_ID);
        assertThat(result).isEqualTo(contract.getCustomer().getCustomerId());
    }

    @Test
    void deleteCustomer_ContractDeleted_ok() {
        classUnderTest.deleteContract(contract);
        verify(mockContractRepository).delete(contract);
    }

}