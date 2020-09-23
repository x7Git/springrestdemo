package com.example.springrestdemo.exception;

import com.example.springrestdemo.exception.error.LengthInvalidException;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ExceptionInfo> handleException(Exception e) {
        if (e instanceof NoEntityFoundException) {
            return exceptionResponse(NOT_FOUND, ExceptionInfo.from(e), e);
        }
        if (e instanceof LengthInvalidException || e instanceof MethodArgumentNotValidException || e instanceof InvalidFormatException) {
            return exceptionResponse(BAD_REQUEST, ExceptionInfo.from(e), e);
        }
        return exceptionResponse(INTERNAL_SERVER_ERROR, ExceptionInfo.from(e), e);
    }

    private ResponseEntity<ExceptionInfo> exceptionResponse(HttpStatus status, ExceptionInfo exceptionInfo, Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return ResponseEntity.status(status).body(exceptionInfo);
    }

}
