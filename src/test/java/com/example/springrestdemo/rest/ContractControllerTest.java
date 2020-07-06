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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ContractControllerTest {

    @Mock
    private ContractService mockContractService;

    @InjectMocks
    private ContractController classUnderTest;

    private Contract contract;
    private static final long CUSTOMER_ID = 3489432L;
    private static final long PRICE = 1599L;
    @BeforeEach
    public void setup(){
        contract = new Contract(ContractType.DSL,PRICE);
    }

    @Test
    void postContract_ContractAdded_ok() {
        //Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(mockContractService.addContract(contract, CUSTOMER_ID)).thenReturn(2L);
        //Act
        ResponseEntity<Long> responseEntity = classUnderTest.postContract(contract, CUSTOMER_ID);
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(2);
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
    void getPrice_calculatePrice_ok(){
        //Arrange
        long price = 7496L;
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(mockContractService.calculatePrice(CUSTOMER_ID)).thenReturn(price);
        //Act
        ResponseEntity<Long> responseEntity = classUnderTest.getPrice(CUSTOMER_ID);
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(price);
    }
}