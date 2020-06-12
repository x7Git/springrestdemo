package com.example.springrestdemo.service;

import com.example.springrestdemo.db.entity.Contract;
import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.repository.ContractRepository;
import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractService {
    private Customer customer;

    private final ContractRepository contractRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, CustomerRepository customerRepository) {
        this.contractRepository = contractRepository;
        this.customerRepository = customerRepository;
    }

    public void deleteContract(Contract contract) {
        contractRepository.delete(contract);
    }

    public Long addContract(Contract contract, long customerId) {
        findCustomer(customerId);
        contract.setCustomer(customer);
            contractRepository.save(contract);
            return contract.getContractId();
    }

    public Long calculatePrice(long customerId) {
        findCustomer(customerId);
        long calculatedPrice = 0L;
        for(Contract contract : customer.getContracts()){
            calculatedPrice += contract.getPrice();
        }
        return calculatedPrice;
    }

    protected void findCustomer (long customerId){
        customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoEntityFoundException(customerId));
    }

}
