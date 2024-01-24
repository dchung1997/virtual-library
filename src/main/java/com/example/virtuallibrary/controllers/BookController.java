package com.example.virtuallibrary.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.virtuallibrary.models.Book;
import com.example.virtuallibrary.models.CategoriesCount;
import com.example.virtuallibrary.models.RatingInfo;
import com.example.virtuallibrary.models.User;
import com.example.virtuallibrary.service.BookService;
import com.example.virtuallibrary.service.UserDetailsServiceImpl;


import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @GetMapping
    public ModelAndView findAll(Pageable pageable) {
        ModelAndView books = new ModelAndView("books");
        Page<Book> foundBooks = bookService.findAllBooks(pageable);

        books.addObject("results", foundBooks);
        books.addObject("page", pageable.getPageNumber());
        books.addObject("totalPages", foundBooks.getTotalPages());    
        return books;
    }

    @GetMapping("/browse")
    public ModelAndView browseAndSearch(@RequestParam(required = false) String context, @RequestParam(required = false) String criteria, Pageable pageable) {
        ModelAndView books = new ModelAndView("browse");
        Page<Book> queryBooks = bookService.findByCriteria(context, criteria, pageable);
        List<CategoriesCount> categoryCount = bookService.getCategoryCount(context);
        String[] delimitedCategories = criteria != null ? criteria.split(",") : new String[]{} ;

        books.addObject("categories", categoryCount);
        books.addObject("results", queryBooks);
        books.addObject("context", context);
        books.addObject("criteria", criteria);
        books.addObject("delimitedCategories", delimitedCategories);
        books.addObject("page", pageable.getPageNumber());
        books.addObject("pageItems", queryBooks.getNumberOfElements());
        books.addObject("totalElements", queryBooks.getTotalElements());
        books.addObject("totalPages", queryBooks.getTotalPages());        
        return books;
    }


    @GetMapping("/{isbn}")    
    public ModelAndView getBook(@PathVariable String isbn) {
        // TODO add error checks.
        ModelAndView bookView = new ModelAndView("book");
        List<Book> books = bookService.findByIsbn(isbn);
        if (books.size() > 0) {
            Book book = books.get(0);
            int totalBooks = books.size();
            int available = bookService.countAvailableBooks(books);
            RatingInfo ratingInfo = bookService.calculateRatingInfo(book);

            
            bookView.addObject("rating", ratingInfo.getWholeStars());
            bookView.addObject("fractionalPart", ratingInfo.getFractionalPart());
            bookView.addObject("available", available);
            bookView.addObject("copies", totalBooks);
            bookView.addObject("book", book);
        }
        return bookView;
    } 
    
    @GetMapping("/{isbn}/hold") 
    @PreAuthorize("isAuthenticated()")
    public ModelAndView holdBook(@PathVariable String isbn, Authentication authentication) {
        // TODO add error checks.
        ModelAndView bookView = new ModelAndView("redirect:/books/" + isbn);

        User user = userDetailsService.findByUserName(authentication.getName());
        List<Book> books = bookService.findByIsbn(isbn);
        for (int i = 0; i < books.size(); i++) {
            Book indexBook = books.get(i);
            if (indexBook.isAvailable()) {
                indexBook.setAvailable(false);
                indexBook.setCurrentUser(user);
                bookService.updateBook(indexBook, indexBook.getId());
            }
        }

        return bookView;
    }    
}
