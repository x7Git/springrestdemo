package com.example.springrestdemo.db.entity;

import com.example.springrestdemo.db.entity.enumeration.ContractType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    @JsonIgnore
    private long contractId;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type")
    private ContractType contractType;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "price")
    private Long price;

    public Contract(ContractType contractType, Long price) {
        this.contractType = contractType;
        this.price = price;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
