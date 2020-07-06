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

    Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({NoEntityFoundException.class})
    public ResponseEntity<String> handleNotFoundException(NoEntityFoundException e) {
        return error(NOT_FOUND, e);
    }

    @ExceptionHandler({LengthInvalidException.class})
    public ResponseEntity<String> handleLengthInvalidException(LengthInvalidException e) {
        return error(NOT_ACCEPTABLE, e);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return error(NOT_ACCEPTABLE, e, "JSON parse error: Cannot deserialize value");
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<String> handleInvalidFormatException(InvalidFormatException e) {
        return error(NOT_ACCEPTABLE, e, "InvalidFormatException: Cannot deserialize value");
    }

/*    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleException(Exception e) {
        return error(INTERNAL_SERVER_ERROR, e);
    }*/

    private ResponseEntity<String> error(HttpStatus status, Exception e, String message) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return ResponseEntity.status(status).body(message);
    }
    private ResponseEntity<String> error(HttpStatus status, Exception e) {
       return error(status, e, e.getMessage());
    }
}
