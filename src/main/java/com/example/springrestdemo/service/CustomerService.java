package com.example.springrestdemo.service;

import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private Customer customer;

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    public Customer getCustomerById(long customerId){
        findCustomer(customerId);
        return customer;
    }

    public void deleteCustomer(long customerId){
        customerRepository.deleteById(customerId);
    }

    public void updateCustomer(Customer customer){
        Customer newCustomer = customerRepository.findByUsername(customer.getUsername())
                .orElseThrow(() -> new NoEntityFoundException(customer.getUsername()));
        if(customer.getName() !=null)
            newCustomer.setName(customer.getName());
        if(customer.getLastName()!=null)
            newCustomer.setLastName(customer.getLastName());
        if(customer.getUsername() !=null)
            newCustomer.setUsername(customer.getUsername());
        customerRepository.save(newCustomer);
    }

    protected void findCustomer (long customerId){
        customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoEntityFoundException(customerId));
    }
}
