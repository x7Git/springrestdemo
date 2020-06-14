package com.example.springrestdemo.service;

import com.example.springrestdemo.db.entity.Customer;
import com.example.springrestdemo.db.repository.CustomerRepository;
import com.example.springrestdemo.exception.error.NoEntityFoundException;
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
public class CustomerDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) {
		String ROLE_PREFIX = "ROLE_";
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new NoEntityFoundException(username));

		if (customer.getRole() != null) {
			return new org.springframework.security.core.userdetails.User(customer.getUsername(), customer.getPassword(),
					Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + customer.getRole())));

		}else{
			return new org.springframework.security.core.userdetails.User(customer.getUsername(), customer.getPassword(),
					new ArrayList<>());
		}
	}
	
	public Customer save(Customer customer) {
		customer.setPassword(bcryptEncoder.encode(customer.getPassword()));
		return customerRepository.save(customer);
	}
}