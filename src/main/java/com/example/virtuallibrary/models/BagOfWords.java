package com.example.virtuallibrary.models;


import java.util.HashMap;

public class BagOfWords {

    private HashMap<String, Double> vector;
    private Book book;

    public BagOfWords(HashMap<String, Double> bagOfWords, Book book) {
        this.vector = bagOfWords;
        this.book = book;
    }

    public BagOfWords() {
        super();
    }

    public HashMap<String, Double> getVector() {
        return vector;
    }

    public void setVector(HashMap<String, Double> vector) {
        this.vector = vector;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
