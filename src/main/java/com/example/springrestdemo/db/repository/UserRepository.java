package com.example.springrestdemo.db.repository;

import com.example.springrestdemo.db.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
  User findByUsername(String username);
}
