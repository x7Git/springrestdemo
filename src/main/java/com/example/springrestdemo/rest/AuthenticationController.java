package com.example.springrestdemo.rest;

import com.example.springrestdemo.authentication.JwtRequest;
import com.example.springrestdemo.authentication.JwtResponse;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.service.CustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<?> postCustomer(@Valid @RequestBody Customer user) {
        customerDetailsService.addCustomer(user);
        var selfLink = linkTo(methodOn(CustomerController.class)
                .getCustomer("")).withSelfRel();
        return ResponseEntity.created(selfLink.toUri()).build();
    }

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestBody JwtRequest changePasswordRequest) {
        return ResponseEntity.noContent().build();

    }

}