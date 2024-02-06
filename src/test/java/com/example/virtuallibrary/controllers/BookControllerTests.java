    package com.example.virtuallibrary.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
        performBrowseTest(requestParams, false, false, "Relevance", 20);
    }

    @Test
    @Transactional
    public void getContextBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("context", "test");
        performBrowseTest(requestParams, true, false, "Relevance", 10);
    }    

    @Test
    @Transactional
    public void getCriteriaBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("criteria", "Fiction");
        performBrowseTest(requestParams, false, true, "Relevance", 20);
    }       
    
    @Test
    @Transactional
    public void getSortBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("sort", "title");
        performBrowseTest(requestParams, false, false, "title", 20);
    }        

    @Test
    @Transactional
    public void getCriteriaSortBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("sort", "title");
        requestParams.add("criteria", "Fiction");
        performBrowseTest(requestParams, false, true, "title", 20);
    }         

    @Test
    @Transactional
    public void getContextCriteriaSortBrowse() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("sort", "title");
        requestParams.add("criteria", "Fiction");
        requestParams.add("context", "test");
        performBrowseTest(requestParams, true, true, "title", 1);
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
        holdBook(isbn, "You have successfully checked out Gilead.", "/books/" + isbn, null);

    }    

    @Test
    @WithMockUser("testuser")
    public void holdAlreadyCheckedOutBook() throws Exception {
        String isbn = "9780002005883";
        holdBook(isbn, "You have successfully checked out Gilead.", "/books/" + isbn, null);
        holdBook(isbn, "You have already checked out this book.", "/books/" + isbn, null);
        returnBook(isbn, "You have successfully returned Gilead.", "/books/" + isbn);
    }   

    @Test
    @Transactional    
    @WithMockUser(value = "testuser", roles = {"ADMIN"})
    public void holdUnavailableBook() throws Exception {
        String isbn = "9780002005999";
        Book book = new Book("9780002005999", "test", "test", "Fiction", "test.com", "String description",
        2002, 3.97, 212, 333, 0, 0);
        book.setAvailable(false);

        LinkedMultiValueMap<String, String> requestParams = createBookParams(book);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        holdBook(isbn, "The book you are trying to check out is unavailable.", "/books/" + isbn, null);
    }       

    @Test
    @Transactional
    @WithMockUser("testuser")
    public void holdInvalidBook() throws Exception {
        String isbn = "9780002005811";
        holdBook(isbn, "The book you were looking for does not exist.", "/home", null);
    }    

    @Test
    @Transactional
    @WithAnonymousUser
    public void holdNoAuthenticationBook() throws Exception {
        String isbn = "9780002005883";
        holdBook(isbn, "Please sign in to access.", "/login", null);
    }    

    @Test
    @WithMockUser("testuser")
    public void returnBook() throws Exception {
        String isbn = "9780002005883";
        holdBook(isbn, "You have successfully checked out Gilead.", "/books/" + isbn, null);
        returnBook(isbn, "You have successfully returned Gilead.", "/books/" + isbn);     
    }    

    @Test
    @Transactional
    public void returnNoUserBook() throws Exception {
        String isbn = "9780002005883";
        returnBook(isbn, "Please sign in to access.", "/login");       
    }    

    @Test
    @Transactional
    @WithMockUser("testuser")
    public void returnNoBook() throws Exception {
        String isbn = "9780002005883";
        returnBook(isbn, "You have not checked out the book.", "/books/" + isbn);       
    }    

    @Test
    @Transactional
    @WithMockUser("testuser")
    public void saveBook() throws Exception {
        String isbn = "9780002005883";
        MockHttpSession session = new MockHttpSession();
        saveBook(isbn, "You have successfully saved Gilead.", null, session);    
    }    


    @Test
    @Transactional
    @WithMockUser("testuser")
    public void saveAlreadySavedBook() throws Exception {
        String isbn = "9780002005883";
        MockHttpSession session = new MockHttpSession();
        saveBook(isbn, "You have successfully saved Gilead.", null, session);    
        saveBook(isbn, "You have already saved this book.", null, session);    
    }     

    @Test
    @Transactional
    @WithMockUser(value = "testuser", roles = {"ADMIN"})
    public void saveUnavailableBook() throws Exception {
        String isbn = "9780002005999";
        MockHttpSession session = new MockHttpSession();

        Book book = new Book("9780002005999", "test", "test", "Fiction", "test.com", "String description",
        2002, 3.97, 212, 333, 0, 0);
        book.setAvailable(false);

        LinkedMultiValueMap<String, String> requestParams = createBookParams(book);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        saveBook(isbn, "The book you are trying to save is unavailable.", null, session);    
    }       

    @Test
    public void saveNoUserBook() throws Exception {
        String isbn = "9780002005883";
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/save", isbn))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }    

    @Test
    @Transactional
    @WithMockUser("testuser")
    public void checkoutBooks() throws Exception  {
        String isbn = "9780002005883";
        MockHttpSession session = new MockHttpSession();
        saveBook(isbn, "You have successfully saved Gilead.", null, session);
        checkoutBook("You have successfull checked out all books.", "/home", null, session);                
    }

    @Test
    @Transactional
    @WithMockUser("testuser")
    public void checkoutMultipleBooks() throws Exception  {
        String isbn = "9780002005883";
        String isbn2 = "9780002261982";
        String isbn3 = "9780006163831";
        
        MockHttpSession session = new MockHttpSession();

        saveBook(isbn, "You have successfully saved Gilead.", null, session);
        saveBook(isbn2, "You have successfully saved Spider's Web.", null, session);
        saveBook(isbn3, "You have successfully saved The One Tree.", null, session);

        checkoutBook("You have successfull checked out all books.", "/home", null, session);                
    }

    @Test
    @Transactional
    public void checkoutUnsuccessfulBook() throws Exception  {

        UserRequestPostProcessor user1Processor = user("testuser3").roles("USER");
        UserRequestPostProcessor user2Processor = user("testuser").roles("ADMIN");

        String isbn = "9780002005999";
        MockHttpSession session = new MockHttpSession();

        Book book = new Book("9780002005999", "test", "test", "Fiction", "test.com", "String description",
        2002, 3.97, 212, 333, 1, 1);
        book.setAvailable(true);

        LinkedMultiValueMap<String, String> requestParams = createBookParams(book);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books").with(user2Processor)
                .params(requestParams))
                .andExpect(MockMvcResultMatchers.status().isCreated());        

        saveBook(isbn, "You have successfully saved test.", user1Processor, session);
        holdBook(isbn, "You have successfully checked out test.", "/books/" + isbn, user2Processor);
        checkoutBook("Unable to checkout the following books.", "/books/checkout", user1Processor, session);

    } 

    private ResultActions saveBook(String isbn, String message, UserRequestPostProcessor userProcessor, MockHttpSession session) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/save", isbn)
                        .with(userProcessor != null ? userProcessor : (request) -> request)// Apply userProcessor if present
                        .session(session))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/books/" + isbn))
        .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
        .andExpect(MockMvcResultMatchers.flash().attribute("message", message));
    }

    private ResultActions returnBook(String isbn, String message, String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/return", isbn))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl(url))
        .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
        .andExpect(MockMvcResultMatchers.flash().attribute("message", message));     
    }

    private ResultActions holdBook(String isbn, String message, String url, UserRequestPostProcessor userProcessor) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}/hold", isbn)
                        .with(userProcessor != null ? userProcessor : (request) -> request))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl(url))
        .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
        .andExpect(MockMvcResultMatchers.flash().attribute("message", message));
    } 
    
    private ResultActions checkoutBook(String message, String url, UserRequestPostProcessor userProcessor, MockHttpSession session) throws Exception {
        return  mockMvc.perform(MockMvcRequestBuilders.get("/books/checkout/hold")
                        .with(userProcessor != null ? userProcessor : (request) -> request)
                        .session(session))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl(url))
        .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
        .andExpect(MockMvcResultMatchers.flash().attribute("message", message));
    }        

    private void performBrowseTest(LinkedMultiValueMap<String, String> requestParams,
                    boolean expectContext, boolean expectCriteria,
                    String expectedSort, int expectedPageItems) throws Exception {
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/books/browse")
                            .params(requestParams))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(MockMvcResultMatchers.model().attributeExists("results"))
                            .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                            .andExpect(MockMvcResultMatchers.model().attributeExists("sort"))
                            .andExpect(MockMvcResultMatchers.model().attribute("page", 0))
                            .andExpect(MockMvcResultMatchers.model().attribute("pageItems", expectedPageItems));

            if (expectContext) {
                    resultActions.andExpect(MockMvcResultMatchers.model().attributeExists("context"));
            } else {
                    resultActions.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("context"));
            }

            if (expectCriteria) {
                    resultActions.andExpect(MockMvcResultMatchers.model().attributeExists("criteria"));
            } else {
                    resultActions.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("criteria"));
            }

            resultActions.andExpect(MockMvcResultMatchers.model().attribute("sort", expectedSort));
            resultActions.andExpect(MockMvcResultMatchers.model().attribute("pageItems", expectedPageItems));
    }

    private LinkedMultiValueMap<String, String> createBookParams(Book book) {
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
        requestParams.add("available_copies", Integer.toString(book.getAvailable_copies()));
        requestParams.add("total_copies", Integer.toString(book.getTotal_copies()));
        requestParams.add("available", Boolean.toString(book.isAvailable()));
        return requestParams;
    }
}
