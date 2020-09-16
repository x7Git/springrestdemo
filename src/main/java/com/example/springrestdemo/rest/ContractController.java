package com.example.springrestdemo.rest;

import com.example.springrestdemo.db.entity.Contract;
import com.example.springrestdemo.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @Secured("ROLE_SERVICE")
    @PostMapping("/" + CtxPath.CONTRACTS + "/" + CtxPath.CUSTOMER_ID_BRACKETS)
    public ResponseEntity<Long> postContract(@RequestBody Contract contract, @PathVariable("customerId") long customerId) {
        var contractResult = contractService.addContract(contract, customerId);
        var selfLink = linkTo(methodOn(CustomerController.class)
                .getCustomer("")).withSelfRel();
        return ResponseEntity.created(selfLink.toUri()).build();
    }

    @Secured("ROLE_SERVICE")
    @DeleteMapping("/" + CtxPath.CONTRACTS + "/" + CtxPath.CONTRACT_ID_BRACKETS)
    public ResponseEntity<String> deleteContract(@PathVariable(CtxPath.CONTRACT_ID) long contractId) {
        contractService.deleteContract(contractId);
        return ResponseEntity.noContent().build();
    }

    @Secured({"ROLE_CUSTOMER", "ROLE_SERVICE"})
    @GetMapping("/" + CtxPath.CALCULATE_PRICE)
    public ResponseEntity<Long> getPrice(@RequestHeader("Authorization") String jwtToken) {
        return ResponseEntity.ok().body(contractService.calculatePrice(jwtToken));
    }

    @Secured("ROLE_SERVICE")
    @GetMapping("/" + CtxPath.CALCULATE_PRICE + "/" + CtxPath.CUSTOMER_ID_BRACKETS)
    public ResponseEntity<Long> getPriceByCustomerId(@PathVariable("customerId") long customerId) {
        return ResponseEntity.ok().body(contractService.calculatePrice(customerId));
    }
}
