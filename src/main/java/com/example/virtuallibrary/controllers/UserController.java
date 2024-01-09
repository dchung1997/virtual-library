package com.example.virtuallibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.virtuallibrary.models.User;
import com.example.virtuallibrary.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable findAllUsers() {
        return userDetailsService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        return userDetailsService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + " || " + "@UserDetailsService.hasId(#id)")
    public void deleteUser(@PathVariable Long id) {
        userDetailsService.deleteUser(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + " || " + "@UserDetailsService.hasId(#id)")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        return userDetailsService.updateUser(user, id);
    }


}
