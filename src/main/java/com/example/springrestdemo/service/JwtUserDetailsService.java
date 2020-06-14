package com.example.springrestdemo.service;

import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.service.DTO.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) {
		String ROLE_PREFIX = "ROLE_";
		Customer customer = customerRepository.findByUsername(username);
		if (customer == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		if (customer.getRole() != null) {
			return new org.springframework.security.core.userdetails.User(customer.getUsername(), customer.getPassword(),
					Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + customer.getRole())));

		}else{
			return new org.springframework.security.core.userdetails.User(customer.getUsername(), customer.getPassword(),
					new ArrayList<>());
		}
	}
	
	public Customer save(CustomerDTO customer) {
		Customer newUser = new Customer(customer.getUsername(), customer.getName(), customer.getLastname(),
				bcryptEncoder.encode(customer.getPassword()), customer.getRole());
		return customerRepository.save(newUser);
	}
}