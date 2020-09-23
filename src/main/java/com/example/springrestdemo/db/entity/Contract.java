package com.example.springrestdemo.db.entity;

import com.example.springrestdemo.db.entity.enumeration.ContractType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Objects;

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
    @Pattern(regexp = "[0-9]+")
    @PositiveOrZero(message = "{contract.price.positiveorzero}")
    private Long price;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    @Column(name = "last_modified_date")
    private LocalDateTime lastUpdateDateTime;

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

    @PrePersist
    public void touchForCreate() {
        creationDateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void touchForUpdate() {
        lastUpdateDateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contract contract = (Contract) o;
        return contractId == contract.contractId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractId);
    }
}
