package com.example.springrestdemo.rest;

import com.example.springrestdemo.db.entity.Contract;
import com.example.springrestdemo.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/" + CtxPath.CONTRACT + "/" + CtxPath.CUSTOMER_ID_BRACKETS)
    public ResponseEntity<Long> postContract(@RequestBody Contract contract, @PathVariable("customerId") long customerId) {
        return ResponseEntity.ok().body(contractService.addContract(contract, customerId));
    }

    @DeleteMapping("/" + CtxPath.CONTRACT)
    public ResponseEntity<String> deleteContract(@RequestBody Contract contract) {
        contractService.deleteContract(contract);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/" + CtxPath.CALCULATE_PRICE + "/" + CtxPath.CUSTOMER_ID_BRACKETS)
    public ResponseEntity<Long> getPrice(@PathVariable(CtxPath.CUSTOMER_ID) long customerId) {
        return ResponseEntity.ok().body(contractService.calculatePrice(customerId));
    }
}
