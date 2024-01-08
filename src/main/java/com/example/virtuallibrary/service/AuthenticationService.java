package com.example.virtuallibrary.service;

import org.springframework.stereotype.Service;

import com.example.virtuallibrary.repository.UserRepository;

@Service
public class AuthenticationService {
    
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
}
