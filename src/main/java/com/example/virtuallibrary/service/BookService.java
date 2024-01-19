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
import com.example.virtuallibrary.models.CategoriesCount;
import com.example.virtuallibrary.models.RatingInfo;
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

    public List<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
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

    public void deleteBook(Long id) {
        bookRepository.findById(id);
        bookRepository.deleteById(id);
    }

    public Book updateBook(Book book, Long id) {
        if (book.getId() != id) {
          throw new BookIdMismatchException();
        }
        bookRepository.findById(id);
        return bookRepository.save(book);
    }

    public Page<Book> findBook(String context, Pageable pageable) {
      return bookRepository.findBy(context, pageable);
    }

    public List<CategoriesCount> getCategoryCount() {
      return bookRepository.getCategoryCount();
    }

    public int countAvailableBooks(List<Book> books) {
      int available = 0;
      for (int i = 0; i < books.size(); i++) {
          if (books.get(i).isAvailable()) {
              available += 1;
          }
      }      
      return available;
    }

  public RatingInfo calculateRatingInfo(Book book) {
      double rating = book.getAverage_rating();
      int wholeStars = (int) rating;
      double fractionalPart = rating - wholeStars;
      return new RatingInfo(wholeStars, fractionalPart);
  }    


}
