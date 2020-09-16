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

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestExceptionHandler {

    Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({NoEntityFoundException.class})
    public ResponseEntity<ExceptionInfo> handleNotFoundException(NoEntityFoundException e) {
        return error(NOT_FOUND, ExceptionInfo.from(e), e);
    }

    @ExceptionHandler({LengthInvalidException.class})
    public ResponseEntity<ExceptionInfo> handleLengthInvalidException(LengthInvalidException e) {
        return error(BAD_REQUEST, ExceptionInfo.from(e), e);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionInfo> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        return error(BAD_REQUEST, ExceptionInfo.from(e), e);
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<ExceptionInfo> handleInvalidFormatException(InvalidFormatException e) {
        return error(BAD_REQUEST, ExceptionInfo.from(e), e);
    }

/*    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleException(Exception e) {
        return error(INTERNAL_SERVER_ERROR, e);
    }*/

    private ResponseEntity<ExceptionInfo> error(HttpStatus status, ExceptionInfo exceptionInfo, Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return ResponseEntity.status(status).body(exceptionInfo);
    }

}
