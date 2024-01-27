package com.example.virtuallibrary.models;


import java.util.HashMap;

public class BagOfWords {

    private HashMap<String, Integer> vector;
    private Book book;

    public BagOfWords(HashMap<String, Integer> vector, Book book) {
        this.vector = vector;
        this.book = book;
    }

    public BagOfWords() {
        super();
    }

    public HashMap<String, Integer> getVector() {
        return vector;
    }

    public void setVector(HashMap<String, Integer> vector) {
        this.vector = vector;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
