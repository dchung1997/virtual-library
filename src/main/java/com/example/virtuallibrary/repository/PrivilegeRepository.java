package com.example.virtuallibrary.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.virtuallibrary.models.Privilege;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {
    public Privilege findByName(String name);
}
