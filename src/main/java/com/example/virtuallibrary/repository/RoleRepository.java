package com.example.virtuallibrary.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.virtuallibrary.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long>  {
    public Role findByName(String name);
    
}
