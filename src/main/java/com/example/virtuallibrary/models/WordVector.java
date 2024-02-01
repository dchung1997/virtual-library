package com.example.virtuallibrary.models;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "counts", columnDefinition = "DOUBLE PRECISION[]")
    private Double[] counts;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public WordVector() {}

    public WordVector(HashMap<String, Double> vector) {
        String[] keys = vector.keySet().toArray(new String[0]);
        Double[] values = vector.values().toArray(new Double[0]);

        this.words = keys;
        this.counts = values;
    }

    public WordVector(Long id, Array wordsArray, Array countsArray) throws SQLException {
        String[] words = (String[]) wordsArray.getArray(); ;
        Double[] counts = (Double[]) countsArray.getArray(); ;

        this.id = id;
        this.words = words; 
        this.counts = counts;
    }

    public double euclideanDistance(WordVector vector, double max) {
        String[] vecWords = vector.getWords();
        Double[] vecCounts = vector.getCounts();

        // Get the shared vocabulary       
        Set<String> commonVocab = new HashSet<>(Arrays.asList(this.words));
        commonVocab.retainAll(Arrays.asList(vecWords));

        // Calculate the squared differences and sum them up
        double sumSquaredDiffs = 0;
        for (String word : commonVocab) {
            int index1 = Arrays.asList(this.words).indexOf(word);
            int index2 = Arrays.asList(vecWords).indexOf(word);

            double diff =  this.counts[index1] - vecCounts[index2];
            sumSquaredDiffs += diff * diff;
        }

        return Math.sqrt(sumSquaredDiffs);
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

    public Double[] getCounts() {
        return counts;
    }

    public void setCounts(Double[] counts) {
        this.counts = counts;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
