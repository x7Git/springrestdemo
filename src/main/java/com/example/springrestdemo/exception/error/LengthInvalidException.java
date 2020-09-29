package com.example.springrestdemo.exception.error;

public class LengthInvalidException extends RuntimeException {

    public LengthInvalidException() {
        super("Username or Password are to long");
    }

}
