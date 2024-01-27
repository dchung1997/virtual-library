package com.example.virtuallibrary.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Book {
 
    @Id
    @Column(unique=true, nullable = false, length=13)
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

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private int available_copies;

    @Column(nullable = false)
    private int total_copies;

    @OneToMany(mappedBy = "book")    
    private List<BookCheckout> checkouts;

    @OneToMany(mappedBy = "book")
    private List<Recommendation> recommendations;

    @OneToOne(mappedBy = "book", fetch = FetchType.EAGER)
    private WordVector bagOfWords;

    public Book() {
        super();
    }

    public Book(String isbn, String title, String author, String categories, String thumbnail, String description,
            int published_year, double average_rating, int num_pages, int ratings_count, int available_copies, int total_copies, WordVector vector) {
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
        this.available = true;
        this.available_copies = available_copies;
        this.total_copies = total_copies;
        this.bagOfWords = vector;
    }

    public String getId() {
        return isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getAvailable_copies() {
        return available_copies;
    }

    public void setAvailable_copies(int available_copies) {
        this.available_copies = available_copies;
    }

    public int getTotal_copies() {
        return total_copies;
    }

    public void setTotal_copies(int total_copies) {
        this.total_copies = total_copies;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }
        
    public List<BookCheckout> getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(List<BookCheckout> checkouts) {
        this.checkouts = checkouts;
    }

    public WordVector getBagOfWords() {
        return bagOfWords;
    }

    public void setBagOfWords(WordVector bagOfWords) {
        this.bagOfWords = bagOfWords;
    }
}