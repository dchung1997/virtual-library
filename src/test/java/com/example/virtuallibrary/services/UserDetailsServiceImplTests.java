package com.example.virtuallibrary.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.virtuallibrary.exceptions.UserAlreadyExistsException;
import com.example.virtuallibrary.exceptions.UserNotFoundException;
import com.example.virtuallibrary.models.User;
import com.example.virtuallibrary.service.UserDetailsServiceImpl;

import jakarta.transaction.Transactional;


@SpringBootTest
@Transactional
public class UserDetailsServiceImplTests {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void testCreateUser() {
        User user = new User("TestUser1", "12345678");

        User createdUser = userDetailsService.createUser(user);
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(createdUser.getRoles().iterator().next().getName(), "ROLE_USER");
    }


    @Test
    public void testDuplicateCreateUser() {
        User user = new User("TestUser1", "12345678");
        User createdUser = userDetailsService.createUser(user);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userDetailsService.createUser(user);
        });        
    }    

    @Test
    public void testDeleteUser() {
        User user = new User("TestUser1", "12345678");
        User createdUser = userDetailsService.createUser(user);
        userDetailsService.deleteUser(createdUser.getId());
    }

    @Test
    public void testEmptyDeleteUser() {
        Long mockId = (long) 99999999;
        assertThrows(UserNotFoundException.class, () -> {
            userDetailsService.deleteUser(mockId);
        });
    }

    @Test
    public void testFindById() {
        User user = new User("TestUser1", "12345678");

        User createdUser = userDetailsService.createUser(user);
        User foundUser = userDetailsService.findUserById(createdUser.getId());
        assertEquals(createdUser, foundUser);
    }

    @Test
    public void testNonExistantFindById() {
        Long mockId = (long) 99999999;
        assertThrows(UserNotFoundException.class, () -> {    
            userDetailsService.findUserById(mockId);
        });
}
