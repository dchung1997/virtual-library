package com.example.virtuallibrary.service;

import java.util.Arrays;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.virtuallibrary.exceptions.UserAlreadyExistsException;
import com.example.virtuallibrary.exceptions.UserIdMismatchException;
import com.example.virtuallibrary.exceptions.UserNotFoundException;
import com.example.virtuallibrary.models.User;
import com.example.virtuallibrary.models.UserDetailsImpl;
import com.example.virtuallibrary.repository.RoleRepository;
import com.example.virtuallibrary.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Iterable findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
          .orElseThrow(UserNotFoundException::new);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
          throw new UserAlreadyExistsException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
          .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    public User updateUser(User user, Long id) {
        if (user.getId() != id) {
          throw new UserIdMismatchException();
        }
        userRepository.findById(id)
          .orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }


    public boolean hasId(Long id) {
      String username =  ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
      User user = userRepository.findByUsername(username);
    
      return user.getId().equals(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
  
        return new UserDetailsImpl(user);
    }
    
}
