package com.example.virtuallibrary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.virtuallibrary.exceptions.BookIdMismatchException;
import com.example.virtuallibrary.exceptions.BookNotFoundException;
import com.example.virtuallibrary.models.Book;
import com.example.virtuallibrary.repository.BookRepository;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;

    public Page<Book> findAllBooks(Pageable pageable) {
      return bookRepository.findAll(pageable);
    }    

    public List<Book> findByTitle(String bookTitle) {
        return bookRepository.findByTitle(bookTitle);
    }

    public Book findByIsbn(String isbn) {
        return bookRepository.findById(isbn);
    }

    public List<Book> findFifteenBooksRandom() {
      return bookRepository.findRandomBooks(PageRequest.of(0, 15));
    }    

    public List<Book> findFifteenBooksFiction() {
      return bookRepository.findFictionBooks(PageRequest.of(0, 15));
    }    

    public List<Book> findFifteenBooksNonFiction() {
      return bookRepository.findNonFictionBooks(PageRequest.of(0, 15));
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(String isbn) {
        bookRepository.findById(isbn);
        bookRepository.deleteById(isbn);
    }

    public Book updateBook(Book book, String isbn) {
        if (book.getIsbn() != isbn) {
          throw new BookIdMismatchException();
        }
        bookRepository.findById(isbn);
        return bookRepository.save(book);
    }


}
