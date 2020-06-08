package com.example.springrestdemo.db.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private long customerId;

    @Column(name ="name")
    private String name;

    @Column(name ="lastname")
    private String lastName;

    @OneToMany(mappedBy = "customer")
    private List<Contract> contracts;

    public Customer(String name, String lastName){
     this.name = name;
     this.lastName = lastName;
    }

    public Customer() {
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

}
