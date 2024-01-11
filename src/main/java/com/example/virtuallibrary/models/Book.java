package com.example.virtuallibrary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Book {
 
    @Id
    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false, length=1000)
    private String title;

    @Column(nullable = false, length=1000)
    private String author;

    @Column(nullable = false)
    private String categories; 

    @Column(nullable = true)
    private String thumbnail;

    @Column(nullable = true, length=99999)
    private String description;

    @Column(nullable = true)
    private int published_year;

    @Column(nullable = true)
    private double average_rating;

    @Column(nullable = true)
    private int num_pages;

    @Column(nullable = true)
    private int ratings_count;
    
    

    public Book() {
        super();
    }

    public Book(String isbn, String title, String author, String categories, String thumbnail, String description,
            int published_year, double average_rating, int num_pages, int ratings_count) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.categories = categories;
        this.thumbnail = thumbnail;
        this.description = description;
        this.published_year = published_year;
        this.average_rating = average_rating;
        this.num_pages = num_pages;
        this.ratings_count = ratings_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPublished_year() {
        return published_year;
    }

    public void setPublished_year(int published_year) {
        this.published_year = published_year;
    }

    public double getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(double average_rating) {
        this.average_rating = average_rating;
    }

    public int getNum_pages() {
        return num_pages;
    }

    public void setNum_pages(int num_pages) {
        this.num_pages = num_pages;
    }

    public int getRatings_count() {
        return ratings_count;
    }

    public void setRatings_count(int ratings_count) {
        this.ratings_count = ratings_count;
    }
    

}