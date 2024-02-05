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

import com.example.virtuallibrary.models.Book;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public void getBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("book"));
    }

    @Test
    @Transactional
    public void getNullBook() throws Exception {
        String isbn = null;
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }    

    @Test
    @Transactional
    public void getInvalidBook() throws Exception {
        String isbn = "9780002005811";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "The book you were looking for does not exist."));                
    }
    

    @Test
    @Transactional
    @WithMockUser("testuser")
    public void holdBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You have successfully checked out Gilead."));
    }    

    @Test
    @Transactional    
    @WithMockUser("testuser")
    public void holdAlreadyCheckedOutBook() throws Exception {
        String isbn = "9780002005883";
        // Initial checkout.
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You have successfully checked out Gilead."));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You have already checked out Gilead."));
    }   

    @Test
    @Transactional    
    @WithMockUser(value = "testuser", roles = {"ADMIN"})
    public void holdUnavailableBook() throws Exception {
        String isbn = "9780002005999";
        Book book = new Book("9780002005999", "test", "test", "Fiction", "test.com", "String description",
        2002, 3.97, 212, 333, 1, 1);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("isbn", book.getId());
        requestParams.add("title", book.getTitle());
        requestParams.add("author", book.getAuthor());
        requestParams.add("categories", book.getCategories());
        requestParams.add("thumbnail", book.getThumbnail());
        requestParams.add("description", book.getDescription());
        requestParams.add("published_year", Integer.toString(book.getPublished_year()));
        requestParams.add("average_rating", Double.toString(book.getAverage_rating()));
        requestParams.add("num_pages", Integer.toString(book.getNum_pages()));
        requestParams.add("ratings_count", Integer.toString(book.getRatings_count()));
        requestParams.add("available_copies", Integer.toString(0));
        requestParams.add("total_copies", Integer.toString(0));
        requestParams.add("available", "false");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "The book you are trying to check out is unavailable."));
    }       

    @Test
    @Transactional
    @WithMockUser("testuser")
    public void holdInvalidBook() throws Exception {
        String isbn = "9780002005811";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "The book you were looking for does not exist."));                
    }    

    @Test
    @Transactional
    @WithAnonymousUser
    public void holdNoAuthenticationBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }    

    @Test
    @Transactional
    @WithMockUser("testuser")
    public void returnBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You have successfully checked out Gilead."));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/return", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn));        
    }    

    @Test
    @Transactional
    public void returnNoUserBook() throws Exception {
        String isbn = "9780002005883";

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/return", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));        
    }    

    @Test
    @Transactional
    @WithMockUser("testuser")
    public void returnNoBook() throws Exception {
        String isbn = "9780002005883";

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/return", isbn))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn));        
    }    

    @Test
    @Transactional
    @WithMockUser("testuser")
    public void saveBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/save", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You have successfully saved Gilead."));

    }    


    @Test
    @Transactional
    @WithMockUser("testuser")
    public void saveAlreadySavedBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/save", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You have successfully saved Gilead."));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/save", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You have already saved Gilead."));
    }     

    @Test
    @Transactional
    @WithMockUser("testuser")    
    public void saveUnavailableBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/save", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn))                
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Gilead cannot be saved as it is unavailable."));        
    }       

    @Test
    public void saveNoUserBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/save", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }    
}
