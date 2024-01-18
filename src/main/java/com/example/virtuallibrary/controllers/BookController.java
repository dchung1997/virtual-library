package com.example.virtuallibrary.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.virtuallibrary.models.Book;
import com.example.virtuallibrary.service.BookService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ModelAndView findAll(Pageable pageable) {
        ModelAndView books = new ModelAndView("books");
        Page<Book> foundBooks = bookService.findAllBooks(pageable);

        books.addObject("results", foundBooks);

        return books;
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam String context, Pageable pageable) {
        ModelAndView books = new ModelAndView("bookSearch");
        Page<Book> queryBooks = bookService.findBook(context, pageable);

        books.addObject("results", queryBooks);
        books.addObject("context", context);
        books.addObject("page", pageable.getPageNumber());
        books.addObject("totalPages", queryBooks.getTotalPages());        
        return books;
    }


    @GetMapping("/{isbn}")    
    public ModelAndView getBook(@PathVariable String isbn) {
        ModelAndView bookView = new ModelAndView("book");
        Book book = bookService.findByIsbn(isbn);
        
        double rating = book.getAverage_rating();
        int wholeStars = (int) rating; 
        double fractionalPart = rating - wholeStars;  
        
        bookView.addObject("rating", wholeStars);
        bookView.addObject("fractionalPart", fractionalPart);
        bookView.addObject("book", book);

        return bookView;
    }    
}
