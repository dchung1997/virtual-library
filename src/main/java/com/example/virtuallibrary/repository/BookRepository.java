package com.example.virtuallibrary.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.virtuallibrary.models.Book;

public interface BookRepository extends CrudRepository<Book, String> {
    List<Book> findByTitle(String title);
    void deleteById(String isbn);
}
