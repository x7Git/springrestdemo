package com.example.springrestdemo.service;

import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var ROLE_PREFIX = "ROLE_";
        var customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new NoEntityFoundException(username));

        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();

        if (customer.getRole() != null) {
            simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(ROLE_PREFIX + customer.getRole()));
        }
        return new org.springframework.security.core.userdetails.User(customer.getUsername(), customer.getPassword(),
                simpleGrantedAuthorityList);
    }
}