package com.example.springrestdemo.db.entity;

import com.example.springrestdemo.db.entity.enumeration.ContractType;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private long contractId;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type")
    private ContractType contractType;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Contract(ContractType contractType) {
        this.contractType = contractType;
    }

    public Contract() {
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public long getContractId() {
        return contractId;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer){
        this.customer = customer;
    }
}
