package com.example.springrestdemo.validation;

import com.example.springrestdemo.db.repository.CustomerRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    CustomerRepository customerRepository;

    public UniqueValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void initialize(Unique constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && !customerRepository.findByUsername(s).isPresent();
    }
}
