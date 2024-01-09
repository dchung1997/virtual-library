package com.example.virtuallibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.virtuallibrary.exceptions.UserAlreadyExistsException;
import com.example.virtuallibrary.models.User;
import com.example.virtuallibrary.service.UserDetailsServiceImpl;

import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
public class AuthController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;



    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        ModelAndView modelAndView = new ModelAndView("login");
        User user = new User();
        modelAndView.addObject("user", user);
        return modelAndView; // Return the login form view
    }

    // @PostMapping("/login")
    // public ModelAndView loginUserAccount(@ModelAttribute("user") User user) {
    //     // Make a call to service and see if it fails.

    // }    
    
    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        User user = new User();
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView registerUserAccount(@ModelAttribute("user") User user) {
        // TODO: Add validation.
        System.out.println("User register has been called for: " + user.getUsername());
        try {
            User registered = userDetailsService.createUser(user);
        } catch (UserAlreadyExistsException uaeEx) {
            ModelAndView modelAndView = new ModelAndView("register", "user", user);
            modelAndView.addObject("message", "An account with that username already exists");
            return modelAndView;
        } catch (RuntimeException ex) {
            return new ModelAndView("register", "user", user);
        }
        return new ModelAndView("home", "user", user);
    }
    
}
