package com.example.springrestdemo.exception.error;

public class CharacterInvalidException extends RuntimeException {

    public CharacterInvalidException() {
        super( "No special characters allowed");
    }

}
