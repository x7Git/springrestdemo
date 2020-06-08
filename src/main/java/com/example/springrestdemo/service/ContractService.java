package com.example.springrestdemo.service;

import com.example.springrestdemo.db.entity.Contract;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.repository.ContractRepository;
import com.example.springrestdemo.db.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, CustomerRepository customerRepository) {
        this.contractRepository = contractRepository;
        this.customerRepository = customerRepository;
    }

    public void deleteAllContractByCustomerId(long customerId) {
        //contractRepository.deleteById();
    }

    public void deleteContract(Contract contract) {

    }

    public Long addContract(Contract contract, long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isPresent()){
            contract.setCustomer(customer.get());
            contractRepository.save(contract);
            return contract.getContractId();
        }
        return null;
    }
}
