package com.example.springrestdemo.service;

import com.example.springrestdemo.authentication.JwtRequest;
import com.example.springrestdemo.authentication.JwtResponse;
import com.example.springrestdemo.authentication.JwtTokenUtil;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import com.example.springrestdemo.rest.ContractController;
import com.example.springrestdemo.rest.CustomerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.Link;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CustomerService {
    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final PasswordEncoder argon2PasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomerDetailsService customerDetailsService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder argon2PasswordEncoder, AuthenticationManager authenticationManager, @Lazy JwtTokenUtil jwtTokenUtil, CustomerDetailsService customerDetailsService) {
        this.customerRepository = customerRepository;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.customerDetailsService = customerDetailsService;
    }

    public List<Customer> getCustomers() {
        logger.debug("get customers");
        List<Customer> customers = customerRepository.findAll();
        for (final Customer customer : customers) {
            addLinks(customer);
        }
        return customers;
    }

    public Customer getCustomerByName(String jwtToken) {
        var jwtTokenWithOutPrefix = jwtToken != null && jwtToken.startsWith("Bearer") ? jwtToken.substring(7) : "";
        String username = jwtTokenUtil.getUsernameFromToken(jwtTokenWithOutPrefix);
        logger.debug("get customer by name: {}", username);
        Customer customer = customerRepository.findByUsername(username).orElseThrow(() -> new NoEntityFoundException(username));
        addLinks(customer);
        return customer;
    }

    public Customer getCustomerById(long customerId) {
        logger.debug("get customer by id: {}", customerId);
        Customer customer = findCustomer(customerId);
        addLinks(customer);
        return customer;
    }

    public void deleteCustomer(long customerId) {
        logger.debug("delete customer by id: {}", customerId);
        customerRepository.deleteById(customerId);
    }

    public void updateCustomer(Customer customer) {
        logger.debug("update customer: {}", customer);
        var newCustomer = customerRepository.findByUsername(customer.getUsername())
                .orElseThrow(() -> new NoEntityFoundException(customer.getUsername()));
        if (customer.getName() != null)
            newCustomer.setName(customer.getName());
        if (customer.getLastName() != null)
            newCustomer.setLastName(customer.getLastName());
        customerRepository.save(newCustomer);
    }


    public Customer addCustomer(Customer customer) {
        customer.setPassword(argon2PasswordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    public void changePassword(JwtRequest changePasswordRequest, String jwtToken) {
        var jwtTokenWithOutPrefix = jwtToken != null && jwtToken.startsWith("Bearer") ? jwtToken.substring(7) : "";
        String username = jwtTokenUtil.getUsernameFromToken(jwtTokenWithOutPrefix);
        Customer customer = customerRepository.findByUsername(username).orElseThrow(() -> new NoEntityFoundException(username));
        if (customer.getUsername().equals(changePasswordRequest.getUsername())) {
            customer.setPassword(argon2PasswordEncoder.encode(changePasswordRequest.getPassword()));
            customerRepository.save(customer);
            logger.debug("change password for customer: {}", username);
        }
        logger.debug("change password for customer: {} has failed", username);

    }

    public JwtResponse authenticate(JwtRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        final var userDetails = customerDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final var token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }

    protected Customer findCustomer(long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NoEntityFoundException(customerId));
    }

    private void addLinks(Customer customer) {
        Link selfLink = linkTo(methodOn(CustomerController.class)
                .getCustomer(customer.getUsername())).withSelfRel();
        customer.add(selfLink);
        Link priceLink = linkTo(methodOn(ContractController.class)
                .getPrice(customer.getUsername())).withSelfRel();
        customer.add(priceLink);
    }

}
