package com.example.virtuallibrary.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import com.example.virtuallibrary.models.User;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTests  {

    @Autowired
    private MockMvc mockMvc;    

    @Test
    public void postRegisterRedirectHome() throws Exception {
        User user = new User("testUser1", "123456789");

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", user.getUsername());
        requestParams.add("password", user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
    }

    @Test
    public void postUserExistsRegister() throws Exception {
        User user = new User("testUser", "123456789");

        String message = "An account with that username already exists.";

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", user.getUsername());
        requestParams.add("password", user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.model().attribute("message", message));
    }

    @Test
    public void postValidationUsernameErrorRegister() throws Exception {
        User user = new User("0", "42311325");

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", user.getUsername());
        requestParams.add("password", user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.model().hasErrors());
    }    

    @Test
    public void postValidationEmptyUsernameRegister() throws Exception {
        User user = new User("", "123456789");

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", user.getUsername());
        requestParams.add("password", user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.model().hasErrors());
    }    

    @Test
    public void postValidationPasswordErrorRegister() throws Exception {
        User user = new User("testUser1", "4231");

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", user.getUsername());
        requestParams.add("password", user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.model().hasErrors());
    }            

    @Test
    public void postValidationEmptyPasswordErrorRegister() throws Exception {
        User user = new User("testUser1", "");

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", user.getUsername());
        requestParams.add("password", user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.model().hasErrors());
    }        


    @Test
    public void postLoginRedirectHome() throws Exception {
        User user = new User("testUser", "password");

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", user.getUsername());
        requestParams.add("password", user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
    }

    @Test
    public void postInvalidLogin() throws Exception {
        User user = new User("test", "password");

        String message = "Invalid Username or Password.";

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", user.getUsername());
        requestParams.add("password", user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.model().attribute("message", message));
    }

    @Test
    public void postBadCredentialLogin() throws Exception {
        User user = new User("testUser", "incorrectPassword");

        String message = "Invalid Username or Password.";

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", user.getUsername());
        requestParams.add("password", user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.model().attribute("message", message));
    }   
}
