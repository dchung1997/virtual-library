package com.example.virtuallibrary.models;

import java.util.HashMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class WordVector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "words", columnDefinition = "text[]")
    private String[] words;

    @Column(name = "counts", columnDefinition = "integer[]")
    private Integer[] counts;


    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public WordVector(HashMap<String, Integer> vector) {
        String[] keys = vector.keySet().toArray(new String[0]);
        Integer[] values = vector.values().toArray(new Integer[0]);

        this.words = keys;
        this.counts = values;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public Integer[] getCounts() {
        return counts;
    }

    public void setCounts(Integer[] counts) {
        this.counts = counts;
    }

    
}
