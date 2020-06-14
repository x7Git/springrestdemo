package com.example.springrestdemo.exception.error;

public class NoEntityFoundException extends RuntimeException {

    public NoEntityFoundException(long customerId) {
        super( "No Entity Found for customerId: "+ customerId);
    }
    public NoEntityFoundException(String username) {
        super( "No Entity Found for username: "+ username);
    }
}
