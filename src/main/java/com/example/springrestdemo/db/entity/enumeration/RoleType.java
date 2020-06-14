package com.example.springrestdemo.db.entity.enumeration;

public enum RoleType {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER"),
    SERVICE("SERVICE");

    private String name;
    RoleType(String name){
        this.name = name;
    }
}
