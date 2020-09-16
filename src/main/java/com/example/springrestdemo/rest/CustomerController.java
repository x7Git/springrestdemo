package com.example.springrestdemo.rest;

import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = "application/hal+json")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Secured({"ROLE_SERVICE", "ROLE_CUSTOMER"})
    @GetMapping("/" + CtxPath.CUSTOMERS)
    public ResponseEntity<Customer> getCustomer(@RequestHeader("Authorization") String jwtToken) {
        return ResponseEntity.ok().body(customerService.getCustomerByName(jwtToken));
    }

    @Secured("ROLE_SERVICE")
    @GetMapping("/" + CtxPath.CUSTOMERS + "/" + CtxPath.CUSTOMER_ID_BRACKETS)
    public ResponseEntity<Customer> getCustomerByCustomerId(@PathVariable("customerId") Long customerId) {
        return ResponseEntity.ok().body(customerService.getCustomerById(customerId));
    }

    @Secured("ROLE_SERVICE")
    @GetMapping("/" + CtxPath.CUSTOMERS + "/all")
    public ResponseEntity<List<Customer>> getCustomers() {
        return ResponseEntity.ok().body(customerService.getCustomers());
    }

    @Secured("ROLE_SERVICE")
    @PutMapping("/" + CtxPath.CUSTOMERS)
    public ResponseEntity<String> putCustomer(@RequestBody Customer customer) {
        customerService.updateCustomer(customer);
        return ResponseEntity.noContent().build();
    }

    @Secured("ROLE_SERVICE")
    @DeleteMapping("/" + CtxPath.CUSTOMERS + "/" + CtxPath.CUSTOMER_ID_BRACKETS)
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
