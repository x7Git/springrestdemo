package com.example.springrestdemo.service;

import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public long addCustomer(Customer customer){
        repository.save(customer);
        return customer.getCustomerId();
    }

    public List<Customer> getCustomers(){
        return repository.findAll();
    }

    public Customer getCustomerById(long customerId){
        return repository.findById(customerId).orElseThrow( () ->
               new NoEntityFoundException(customerId));
    }

    public void deleteCustomer(long customerId){
        repository.deleteById(customerId);
    }

    public void updateCustomer(Customer customer){
        Customer existingCustomer = repository.findById(customer.getCustomerId()).orElseThrow( () ->
                new NoEntityFoundException(customer.getCustomerId()));
        existingCustomer.setName(customer.getName());
        existingCustomer.setLastName(customer.getLastName());
        repository.save(existingCustomer);
    }
}
