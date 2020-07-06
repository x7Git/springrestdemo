package com.example.springrestdemo.exception;

import com.example.springrestdemo.exception.error.LengthInvalidException;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class RestExceptionHandlerTest {
    @InjectMocks
    private RestExceptionHandler classUnderTest;

    @Test
    void handleNotFoundException() {
        //Act
        ResponseEntity<?> result = classUnderTest.handleNotFoundException(mock(NoEntityFoundException.class));
        //Assert
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void handleLengthInvalidException() {
        //Act
        ResponseEntity<?>  result = classUnderTest.handleLengthInvalidException(mock(LengthInvalidException.class));
        //Assert
        assertThat(result.getStatusCode()).isEqualTo(NOT_ACCEPTABLE);
    }

    @Test
    void handleMethodArgumentNotValidException() {
        //Act
        ResponseEntity<?>  result = classUnderTest.handleMethodArgumentNotValidException(mock(MethodArgumentNotValidException.class));
        //Assert
        assertThat(result.getStatusCode()).isEqualTo(NOT_ACCEPTABLE);
    }

    @Test
    void handleInvalidFormatException() {
        //Act
        ResponseEntity<?>  result = classUnderTest.handleInvalidFormatException(mock(InvalidFormatException.class));
        //Assert
        assertThat(result.getStatusCode()).isEqualTo(NOT_ACCEPTABLE);
    }
}