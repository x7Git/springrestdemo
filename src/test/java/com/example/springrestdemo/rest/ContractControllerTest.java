package com.example.springrestdemo.rest;

import com.example.springrestdemo.db.entity.Contract;
import com.example.springrestdemo.db.entity.enumeration.ContractType;
import com.example.springrestdemo.service.ContractService;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ContractControllerTest {
    private static final String JWT_TOKEN_BEARER = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImV4cCI6MTU5MjE1MTg3OSwiaWF0IjoxNTkyMTMzODc5fQ._pdSAQ1to8m181swsja4tF7bB-zteJzxx3gQMaJx5jbRzVcHo7hWrLgQlixh_yOyiLZ-Z7JcFCvINVkAbUGr6Q";
    private static final long CUSTOMER_ID = 3489432L;
    private static final long PRICE = 1599L;
    @Mock
    private ContractService mockContractService;
    @InjectMocks
    private ContractController classUnderTest;
    private Contract contract;

    @BeforeEach
    public void setup() {
        contract = new Contract(ContractType.DSL, PRICE);
    }

    @Test
    void postContract_ContractAdded_ok() {
        //Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(mockContractService.addContract(contract, CUSTOMER_ID)).thenReturn(mock(Contract.class));
        //Act
        ResponseEntity<Long> responseEntity = classUnderTest.postContract(contract, CUSTOMER_ID);
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().get("Location")).isNotNull();
    }

    @Test
    void deleteContract_contractDeleted_ok() {
        //Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        //Act
        ResponseEntity<String> responseEntity = classUnderTest.deleteContract(14L);
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    void getPrice_calculatePrice_ok() {
        //Arrange
        long price = 7496L;
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(mockContractService.calculatePrice(JWT_TOKEN_BEARER)).thenReturn(price);
        //Act
        ResponseEntity<Long> responseEntity = classUnderTest.getPrice(JWT_TOKEN_BEARER);
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(price);
    }
}