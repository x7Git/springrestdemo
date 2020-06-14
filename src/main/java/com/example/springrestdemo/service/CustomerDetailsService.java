package com.example.springrestdemo.service;

import com.example.springrestdemo.authentication.JwtRequest;
import com.example.springrestdemo.authentication.JwtResponse;
import com.example.springrestdemo.authentication.JwtTokenUtil;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String username) {
        String ROLE_PREFIX = "ROLE_";
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new NoEntityFoundException(username));

        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();

        if (customer.getRole() != null) {
            simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(ROLE_PREFIX + customer.getRole()));
        }
        return new org.springframework.security.core.userdetails.User(customer.getUsername(), customer.getPassword(),
                simpleGrantedAuthorityList);
    }

    public Customer addCustomer(Customer customer) {
        customer.setPassword(bcryptEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    public JwtResponse authenticate(JwtRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        final UserDetails userDetails = customerDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }
}