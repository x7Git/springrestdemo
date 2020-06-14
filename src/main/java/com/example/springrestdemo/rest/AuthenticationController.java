package com.example.springrestdemo.rest;

import com.example.springrestdemo.authentication.JwtRequest;
import com.example.springrestdemo.authentication.JwtResponse;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.service.CustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AuthenticationController {

    private final CustomerDetailsService customerDetailsService;

    @Autowired
    public AuthenticationController(CustomerDetailsService customerDetailsService) {
        this.customerDetailsService = customerDetailsService;
    }

    @PostMapping("/" + CtxPath.LOG_IN)
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        return ResponseEntity.ok(customerDetailsService.authenticate(authenticationRequest));
    }

    @PostMapping("/" + CtxPath.SIGN_IN)
    public ResponseEntity<?> postCustomer(@RequestBody Customer user) {
        return ResponseEntity.ok(customerDetailsService.addCustomer(user));
    }
}