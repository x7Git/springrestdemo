package com.example.springrestdemo.service;

import com.example.springrestdemo.authentication.JwtTokenUtil;
import com.example.springrestdemo.db.entity.Contract;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.repository.ContractRepository;
import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ContractService {
    private final Logger logger = LoggerFactory.getLogger(ContractService.class);
    private final ContractRepository contractRepository;
    private final CustomerRepository customerRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ContractService(ContractRepository contractRepository, @Lazy CustomerRepository customerRepository, @Lazy JwtTokenUtil jwtTokenUtil) {
        this.contractRepository = contractRepository;
        this.customerRepository = customerRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public void deleteContract(long contractId) {
        logger.debug("delete contract by id: {}", contractId);
        contractRepository.deleteById(contractId);
    }

    public Contract addContract(Contract contract, long customerId) {
        logger.debug("assign a contract: {} to a customerId: {}", contract, customerId);
        contract.setCustomer(findCustomer(customerId));
        contractRepository.save(contract);
        return contract;
    }

    public long calculatePrice(String jwtToken) {
        var jwtTokenWithOutPrefix = jwtToken != null && jwtToken.startsWith("Bearer") ? jwtToken.substring(7) : "";
        String username = jwtTokenUtil.getUsernameFromToken(jwtTokenWithOutPrefix);
        logger.debug("calculatePrice for customer: {}", username);
        var customer = customerRepository.findByUsername(username).orElseThrow(() -> new NoEntityFoundException(username));
        var calculatedPrice = 0L;
        if (customer.getContracts() != null) {
            for (Contract contract : customer) {
                calculatedPrice += contract.getPrice();
            }
        }
        return calculatedPrice;
    }

    public long calculatePrice(long customerId) {
        logger.debug("calculatePrice for customerId: {}", customerId);
        var customer = findCustomer(customerId);
        var calculatedPrice = 0L;
        if (customer.getContracts() != null) {
            for (Contract contract : customer) {
                calculatedPrice += contract.getPrice();
            }
        }
        return calculatedPrice;
    }

    protected Customer findCustomer(long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NoEntityFoundException(customerId));
    }

}
