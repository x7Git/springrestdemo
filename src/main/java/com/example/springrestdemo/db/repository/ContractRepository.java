package com.example.springrestdemo.db.repository;

import com.example.springrestdemo.db.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
