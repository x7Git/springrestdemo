package com.example.springrestdemo.rest;

import com.example.springrestdemo.authentication.JwtRequest;
import com.example.springrestdemo.authentication.JwtResponse;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
public class AuthenticationController {

    private final CustomerService customerService;

    @Autowired
    public AuthenticationController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/" + CtxPath.LOG_IN)
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        return ResponseEntity.ok(customerService.authenticate(authenticationRequest));
    }

    @PostMapping("/" + CtxPath.SIGN_IN)
    public ResponseEntity<?> postCustomer(@Valid @RequestBody Customer user) {
        customerService.addCustomer(user);
        var selfLink = linkTo(methodOn(CustomerController.class)
                .getCustomer("")).withSelfRel();
        return ResponseEntity.created(selfLink.toUri()).build();
    }

}