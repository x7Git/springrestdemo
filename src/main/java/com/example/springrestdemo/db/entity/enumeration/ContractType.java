package com.example.springrestdemo.db.entity.enumeration;

public enum ContractType {
    DSL("dsl"),
    MOBILE("mobile"),
    IPTV("iptv");

    private String name;

    ContractType(String name){
        this.name = name;
    }

}
