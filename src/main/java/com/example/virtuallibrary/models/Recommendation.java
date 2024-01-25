package com.example.virtuallibrary.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "recommended_book_id")
    private Book recommendedBook;

    public Recommendation() {
        super();
    }

    public Recommendation(Book book, Book recommendedBook) {
        this.book = book;
        this.recommendedBook = recommendedBook;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getRecommendedBook() {
        return recommendedBook;
    }

    public void setRecommendedBook(Book recommendedBook) {
        this.recommendedBook = recommendedBook;
    }
}
