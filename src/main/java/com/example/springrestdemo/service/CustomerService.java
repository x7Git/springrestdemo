package com.example.springrestdemo.service;

import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import com.example.springrestdemo.rest.ContractController;
import com.example.springrestdemo.rest.CustomerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CustomerService {
    private Customer customer;

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomers(){
        List<Customer> customers = customerRepository.findAll();
        for (final Customer customer : customers) {
            Link selfLink = linkTo(methodOn(CustomerController.class)
                    .getCustomer(customer.getCustomerId())).withSelfRel();
            customer.add(selfLink);
            Link priceLink = linkTo(methodOn(ContractController.class)
                    .getPrice(customer.getCustomerId())).withSelfRel();
            customer.add(priceLink);
        }
        return customers;
    }

    public Customer getCustomerById(long customerId){
        findCustomer(customerId);
        return customer;
    }

    public void deleteCustomer(long customerId){
        customerRepository.deleteById(customerId);
    }

    public void updateCustomer(Customer customer){
        var newCustomer = customerRepository.findByUsername(customer.getUsername())
                .orElseThrow(() -> new NoEntityFoundException(customer.getUsername()));
        if(customer.getName() !=null)
            newCustomer.setName(customer.getName());
        if(customer.getLastName()!=null)
            newCustomer.setLastName(customer.getLastName());
        customerRepository.save(newCustomer);
    }

    protected void findCustomer (long customerId){
        customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoEntityFoundException(customerId));
    }

}
