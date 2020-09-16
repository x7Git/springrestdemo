package com.example.springrestdemo.rest;

import com.example.springrestdemo.db.entity.Contract;
import com.example.springrestdemo.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/" + CtxPath.CONTRACT + "/" + CtxPath.CUSTOMER_ID_BRACKETS)
    public ResponseEntity<Long> postContract(@RequestBody Contract contract, @PathVariable("customerId") long customerId) {
        var contractResult = contractService.addContract(contract, customerId);
        var selfLink = linkTo(methodOn(CustomerController.class)
                .getCustomer(contractResult.getContractId())).withSelfRel();
        return ResponseEntity.created(selfLink.toUri()).build();
    }

    @DeleteMapping("/" + CtxPath.CONTRACT + "/" + CtxPath.CONTRACT_ID_BRACKETS)
    public ResponseEntity<String> deleteContract(@PathVariable(CtxPath.CONTRACT_ID) long contractId) {
        contractService.deleteContract(contractId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/" + CtxPath.CALCULATE_PRICE + "/" + CtxPath.CUSTOMER_ID_BRACKETS)
    public ResponseEntity<Long> getPrice(@PathVariable(CtxPath.CUSTOMER_ID) long customerId) {
        return ResponseEntity.ok().body(contractService.calculatePrice(customerId));
    }
}
