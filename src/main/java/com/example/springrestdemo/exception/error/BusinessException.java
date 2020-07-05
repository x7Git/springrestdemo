package com.example.springrestdemo.exception.error;

public class BusinessException extends RuntimeException {

    public BusinessException() {
        super( "Username or Password are to long");
    }

}
