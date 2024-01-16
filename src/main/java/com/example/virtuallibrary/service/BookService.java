package com.example.virtuallibrary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Iterable<Book> findAllBooks() {
        return bookRepository.findAll();
    } 

    public List<Book> findByTitle(String bookTitle) {
        return bookRepository.findByTitle(bookTitle);
    }

    public Book findByIsbn(String isbn) {
        return bookRepository.findById(isbn)
          .orElseThrow(BookNotFoundException::new);
    }

    public List<Book> findFifteenBooksByCategory(String genre) {
        return bookRepository.findByCategories(genre, PageRequest.of(0, 15));
    }

    public List<Book> findFifteenBooksRandom() {
      return bookRepository.findRandomBooks(PageRequest.of(0, 15));
  }    

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(String isbn) {
        bookRepository.findById(isbn)
          .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(isbn);
    }

    public Book updateBook(Book book, String isbn) {
        if (book.getIsbn() != isbn) {
          throw new BookIdMismatchException();
        }
        bookRepository.findById(isbn)
          .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

}
