package com.example.virtuallibrary.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.virtuallibrary.models.Book;
import com.example.virtuallibrary.service.BookService;

@Controller
public class HomeController {
    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private BookService bookService;

    @GetMapping({"/", "/home"})
    public ModelAndView homePage() {
        ModelAndView home = new ModelAndView("home");
        List<Book> randomBooks = bookService.findFifteenBooksRandom();
        List<Book> fictionalBooks = bookService.findFifteenBooksFiction();
        List<Book> nonFictionalBooks = bookService.findFifteenBooksNonFiction();


        home.addObject("random5", randomBooks.subList(0, 5));
        home.addObject("random10", randomBooks.subList(5, 10));
        home.addObject("random15", randomBooks.subList(10, 15));

        home.addObject("fiction5", fictionalBooks.subList(0, 5));
        home.addObject("fiction10", fictionalBooks.subList(5, 10));
        home.addObject("fiction15", fictionalBooks.subList(10, 15));

        home.addObject("nonFiction5", nonFictionalBooks.subList(0, 5));
        home.addObject("nonFiction10", nonFictionalBooks.subList(5, 10));
        home.addObject("nonFiction15", nonFictionalBooks.subList(10, 15));

        home.addObject("appName", appName);
        return home;
    }    
}
