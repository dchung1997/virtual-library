package com.example.virtuallibrary.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.virtuallibrary.models.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, String> {
    List<Book> findByTitle(String title);
    List<Book> findByCategories(String categories, PageRequest pageRequest);
    @Query("SELECT b FROM Book b WHERE categories = 'Fiction' ORDER BY RANDOM()" )
    List<Book> findFictionBooks(PageRequest pageRequest);
    @Query("SELECT b FROM Book b WHERE categories LIKE '%Nonfiction%' OR categories LIKE '%Business%' OR categories LIKE '%Self-Help%' OR categories LIKE '%Art%' OR categories LIKE '%Philosophy%' OR categories LIKE '%Psychology%' OR categories LIKE '%Sports%' OR categories LIKE '%Poetry%' OR categories LIKE '%History%' ORDER BY RANDOM()" )
    List<Book> findNonFictionBooks(PageRequest pageRequest);
    @Query("SELECT b FROM Book b ORDER BY RANDOM()")
    List<Book> findRandomBooks(PageRequest pageRequest);
    void deleteById(String id);
    Page<Book> findAll(Pageable pageable);
    Book save(Book book);
    Book findById(String id);
    Book findByIsbn(String isbn);
}
