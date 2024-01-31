package com.example.virtuallibrary.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookControllerTests {

	@Autowired
	private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;    

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				    .webAppContextSetup(context)
				    .apply(SecurityMockMvcConfigurers.springSecurity()) 
				    .build();
	}

    @Test
    public void getBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        mockMvc.perform(MockMvcRequestBuilders.get("/books/browse")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("results"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("context"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("criteria"))
                .andExpect(MockMvcResultMatchers.model().attribute("sort", "Relevance"))
                .andExpect(MockMvcResultMatchers.model().attribute("page", 0))
                .andExpect(MockMvcResultMatchers.model().attribute("pageItems", 20));
    }

    @Test
    public void getContextBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("context", "test");

        mockMvc.perform(MockMvcRequestBuilders.get("/books/browse")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("results"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("context"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("criteria"))
                .andExpect(MockMvcResultMatchers.model().attribute("sort", "Relevance"))
                .andExpect(MockMvcResultMatchers.model().attribute("page", 0))
                .andExpect(MockMvcResultMatchers.model().attribute("pageItems", 10));
    }    

    @Test
    public void getCriteriaBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("criteria", "Fiction");

        mockMvc.perform(MockMvcRequestBuilders.get("/books/browse")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("results"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("context"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("criteria"))
                .andExpect(MockMvcResultMatchers.model().attribute("sort", "Relevance"))
                .andExpect(MockMvcResultMatchers.model().attribute("page", 0))
                .andExpect(MockMvcResultMatchers.model().attribute("pageItems", 20));
    }       
    
    @Test
    public void getSortBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("sort", "title");

        mockMvc.perform(MockMvcRequestBuilders.get("/books/browse")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("results"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("context"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("criteria"))
                .andExpect(MockMvcResultMatchers.model().attribute("sort", "title"))
                .andExpect(MockMvcResultMatchers.model().attribute("page", 0))
                .andExpect(MockMvcResultMatchers.model().attribute("pageItems", 20));
    }        

    @Test
    public void getCriteriaSortBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("sort", "title");
        requestParams.add("criteria", "Fiction");

        mockMvc.perform(MockMvcRequestBuilders.get("/books/browse")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("results"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("context"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("criteria"))
                .andExpect(MockMvcResultMatchers.model().attribute("sort", "title"))
                .andExpect(MockMvcResultMatchers.model().attribute("page", 0))
                .andExpect(MockMvcResultMatchers.model().attribute("pageItems", 20));
    }         

    @Test
    public void getContextCriteriaSortBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("sort", "title");
        requestParams.add("criteria", "Fiction");
        requestParams.add("context", "test");

        mockMvc.perform(MockMvcRequestBuilders.get("/books/browse")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("results"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("context"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("criteria"))
                .andExpect(MockMvcResultMatchers.model().attribute("sort", "title"))
                .andExpect(MockMvcResultMatchers.model().attribute("page", 0))
                .andExpect(MockMvcResultMatchers.model().attribute("pageItems", 1));
    }        

    @Test
    public void getBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("book"));
    }

    @Test
    public void getNullBook() throws Exception {
        String isbn = null;
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }    

    @Test
    public void getInvalidBook() throws Exception {
        String isbn = "9780002005811";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home?message=The+book+you+were+looking+for+does+not+exist"));
    }
    

    @Test
    @WithMockUser("testuser")
    public void holdBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn));
    }    

    @Test
    @WithMockUser("testuser")
    public void holdInvalidBook() throws Exception {
        String isbn = "9780002005811";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home?message=The+book+you+were+looking+for+does+not+exist"));
    }    

    @Test
    @WithAnonymousUser
    public void holdNoAuthenticationBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }    
}
