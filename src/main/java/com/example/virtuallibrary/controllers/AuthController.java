package com.example.virtuallibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.example.virtuallibrary.exceptions.UserAlreadyExistsException;
import com.example.virtuallibrary.models.User;
import com.example.virtuallibrary.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@RestController
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public ModelAndView showLoginForm(@RequestParam(required = false) String message) {
        ModelAndView modelAndView = new ModelAndView("login");
        
        if (message != null && message != "") {
            modelAndView.addObject("message", message);
        }

        User user = new User();
        modelAndView.addObject("user", user);
        return modelAndView; // Return the login form view
    }

    @PostMapping("/login")
    public ModelAndView loginUserAccount(@Valid @ModelAttribute User user, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authenticationResponse);

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);            
        } catch (AuthenticationException ex) {
            ModelAndView modelAndView = new ModelAndView("login", "user", new User());
            modelAndView.addObject("message", "Invalid Username or Password.");  
            response.setStatus(HttpStatus.BAD_REQUEST.value());                    
            return modelAndView;            
        } catch (RuntimeException ex) {
            ModelAndView modelAndView = new ModelAndView("login", "user", new User());
            modelAndView.addObject("message", "Invalid Username or Password.");  
            response.setStatus(HttpStatus.BAD_REQUEST.value());                    
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
    public ModelAndView registerUserAccount(@Valid @ModelAttribute User user, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("register", "user", user);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            modelAndView.addObject("errors", bindingResult.getAllErrors());               
            return modelAndView;
        }

        String username = user.getUsername();
        String password = user.getPassword();

        try {
            User registered = userDetailsService.createUser(user);
            Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
            SecurityContextHolder.getContext().setAuthentication(authenticationResponse);   
            response.setStatus(HttpStatus.CREATED.value());
        } catch (UserAlreadyExistsException uaeEx) {
            ModelAndView modelAndView = new ModelAndView("register", "user", user);
            modelAndView.addObject("message", "An account with that username already exists.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return modelAndView;
        } catch (RuntimeException ex) {
            ModelAndView modelAndView = new ModelAndView("register", "user", user);
            modelAndView.addObject("message", "An unexpected error occurred.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return modelAndView;
        }

        return new ModelAndView("redirect:/home");
    }
    
}
