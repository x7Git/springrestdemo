package com.example.springrestdemo.db.entity;

import com.example.springrestdemo.db.entity.enumeration.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    @JsonIgnore
    private long customerId;

    @Column(name = "username")
    private String username;

    @Column(name ="name")
    private String name;

    @Column(name ="lastname")
    private String lastName;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<Contract> contracts;

    @Column(name ="password")
    @JsonIgnore
    private String password;

    @Column(name ="role")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    public Customer(String username, String name, String lastName, String password, RoleType role) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
    }

    public Customer() {
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
