package com.example.springrestdemo.rest;

import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.service.CustomerService;
import com.example.springrestdemo.service.DTO.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/" + CtxPath.CUSTOMER + "/" + CtxPath.CUSTOMER_ID_BRACKETS)
    public ResponseEntity<Customer> getCustomer(@PathVariable("customerId") Long customerId) {
        return ResponseEntity.ok().body(customerService.getCustomerById(customerId));
    }

   @GetMapping("/" + CtxPath.CUSTOMERS)
    public ResponseEntity<List<Customer>> getCustomers() {
        return ResponseEntity.ok().body(customerService.getCustomers());
    }

    @PutMapping("/" + CtxPath.CUSTOMER)
    public ResponseEntity<String> putCustomer(@RequestBody CustomerDTO customer) {
        customerService.updateCustomer(customer);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/" + CtxPath.CUSTOMER + "/" + CtxPath.CUSTOMER_ID_BRACKETS)
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId")Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
