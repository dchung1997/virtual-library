package com.example.virtuallibrary.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.virtuallibrary.models.User;


public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Boolean existsByUsername(String username);
}
