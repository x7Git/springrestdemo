package com.example.springrestdemo.rest;

import java.util.List;

public final class CtxPath {

    public static final String CUSTOMER_ID = "customerId";
    public static final String CUSTOMER_ID_BRACKETS = "{customerId}";
    public static final String CONTRACTS = "contracts";
    public static final String CUSTOMERS = "customers";
    public static final String CALCULATE_PRICE = "calculateprice";
    public static final String SYSTEM_ALIVE = "systemalive";
    public static final String SIGN_IN = "signin";
    public static final String LOG_IN = "login";
    public static final String OPEN_API = "api-docs/**";
    public static final String SWAGGER_UI = "swagger-ui/**";
    public static final String SWAGGER_HTML = "swagger.html";
    public static final String CONTRACT_ID_BRACKETS = "{contractId}";
    public static final String CONTRACT_ID = "contractId";
    public static final List<String> PATHS_WITHOUT_AUTHENTICATION = List.of("/" + LOG_IN, "/" + SIGN_IN, "/" + OPEN_API, "/" + SYSTEM_ALIVE, "/" + SWAGGER_HTML, "/" + SWAGGER_UI);

    private CtxPath() {
        // for noninstanibilty
        throw new AssertionError();
    }
}
