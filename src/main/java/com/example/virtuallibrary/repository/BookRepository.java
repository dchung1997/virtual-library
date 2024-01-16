package com.example.virtuallibrary.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.virtuallibrary.models.Book;

public interface BookRepository extends CrudRepository<Book, String> {
    List<Book> findByTitle(String title);
    List<Book> findByCategories(String categories, PageRequest pageRequest);
    @Query("SELECT b FROM Book b ORDER BY RANDOM()")
    List<Book> findRandomBooks(PageRequest pageRequest);
    void deleteById(String isbn);
}
