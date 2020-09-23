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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class RestExceptionHandlerTest {
    @InjectMocks
    private RestExceptionHandler classUnderTest;

    @Test
    void handleNotFoundException() {
        //Act
        ResponseEntity<?> result = classUnderTest.handleException(mock(NoEntityFoundException.class));
        //Assert
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void handleLengthInvalidException() {
        //Act
        ResponseEntity<?> result = classUnderTest.handleException(mock(LengthInvalidException.class));
        //Assert
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void handleMethodArgumentNotValidException() {
        //Arrange
        MethodArgumentNotValidException mockMethodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(mockMethodArgumentNotValidException.getBindingResult()).thenReturn(mock(BindingResult.class));
        //Act
        ResponseEntity<?> result = classUnderTest.handleException(mockMethodArgumentNotValidException);
        //Assert
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void handleInvalidFormatException() {
        //Act
        ResponseEntity<?> result = classUnderTest.handleException(mock(InvalidFormatException.class));
        //Assert
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
    }
}