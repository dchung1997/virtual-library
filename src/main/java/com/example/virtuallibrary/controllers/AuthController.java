package com.example.virtuallibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.virtuallibrary.exceptions.UserAlreadyExistsException;
import com.example.virtuallibrary.models.User;
import com.example.virtuallibrary.service.UserDetailsServiceImpl;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        ModelAndView modelAndView = new ModelAndView("login");
        User user = new User();
        modelAndView.addObject("user", user);
        return modelAndView; // Return the login form view
    }

    @PostMapping("/login")
    public ModelAndView loginUserAccount(@ModelAttribute("user") User user) {
        // Make a call to service and see if it fails.
        try {
            Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
            SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
        } catch (BadCredentialsException bdcdEx) {
            ModelAndView modelAndView = new ModelAndView("login", "user", user);
            modelAndView.addObject("message", "Invalid Username or Password.");          
            return modelAndView;
        } catch (RuntimeException ex) {
            ModelAndView modelAndView = new ModelAndView("login", "user", user);
            modelAndView.addObject("message", "An unknown error has occurred.");          
            return modelAndView;
        }
        return new ModelAndView("redirect:/home");
    }    
    
    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        User user = new User();
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView registerUserAccount(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        // TODO: Add validation.
        String username = user.getUsername();
        String password = user.getPassword();
        try {
            User registered = userDetailsService.createUser(user);
            Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
            SecurityContextHolder.getContext().setAuthentication(authenticationResponse);        
        } catch (UserAlreadyExistsException uaeEx) {
            ModelAndView modelAndView = new ModelAndView("register", "user", user);
            modelAndView.addObject("message", "An account with that username already exists");
            return modelAndView;
        } catch (RuntimeException ex) {
            return new ModelAndView("register", "user", user);
        }

        return new ModelAndView("home");
    }
    
}
