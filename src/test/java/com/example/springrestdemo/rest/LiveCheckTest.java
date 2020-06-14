package com.example.springrestdemo.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class LiveCheckTest {
    @InjectMocks
    private LiveCheck classUnderTest;

    @Test
    void systemAlive_serverIsRunning_ok() {
        //Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        //Act
        ResponseEntity<String> responseEntity = classUnderTest.systemAlive();
        //Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
}