package com.example.springrestdemo.db.entity;

import com.example.springrestdemo.db.entity.enumeration.RoleType;
import com.example.springrestdemo.validation.Unique;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer extends RepresentationModel<Customer> implements Iterable<Contract> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    @JsonIgnore
    private long customerId;

    @Unique(message = "{account.username.unique}")
    @Column(name = "username", nullable = false)
    @Size(min = 3, max = 25, message = "{account.username.size }")
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String username;

    @Column(name = "name")
    @Size(min = 2, max = 20, message = "{account.name.size}")
    @Pattern(regexp = "[A-Z][a-z]+")
    private String name;

    @Column(name = "lastname")
    @Size(min = 2, max = 20, message = "{account.lastname.size")
    @Pattern(regexp = "[A-ZÄÖÜ][a-zäöü]+")
    private String lastName;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<Contract> contracts;

    @Column(name = "password", nullable = false)
    @Size(min = 5, max = 64, message = "{account.password.size}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "role")
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

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
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

    @Override
    public Iterator<Contract> iterator() {
        return contracts.iterator();
    }
}
