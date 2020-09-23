package com.example.springrestdemo.service;

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
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CustomerService {
    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, @Lazy JwtTokenUtil jwtTokenUtil) {
        this.customerRepository = customerRepository;
        this.jwtTokenUtil = jwtTokenUtil;
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
