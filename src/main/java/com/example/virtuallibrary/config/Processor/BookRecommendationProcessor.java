package com.example.virtuallibrary.config.Processor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.virtuallibrary.models.Book;
import com.example.virtuallibrary.models.Neighbor;
import com.example.virtuallibrary.models.Recommendation;
import com.example.virtuallibrary.models.WordVector;
import com.example.virtuallibrary.repository.WordVectorRepository;


public class BookRecommendationProcessor implements ItemProcessor<Book,  List<Recommendation>>{
    @Autowired
    private WordVectorRepository wordVectorRepository;

    private List<WordVector> vectors;

    // I want to store in memory values from bookRepository for continued use.
    public BookRecommendationProcessor() {
        super();
        this.vectors = null;
    }

    @Override
    // Implements a K-NN Approach using Bag of Words.
    public List<Recommendation> process(Book book) {
        if (this.vectors == null) {
            this.vectors = wordVectorRepository.findAll();
        }

        WordVector bag = book.getBagOfWords();
        PriorityQueue<Neighbor> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(Neighbor::getDistance));
        // Compare distance between vectors.
        for (WordVector vector : vectors) {
            if (bag.getId() != vector.getId()) {
                double distance = bag.euclideanDistance(vector);
                priorityQueue.add(new Neighbor(vector, distance));
            }
        }

        // Generates a list of recommendations.
        List<Recommendation> recommendations = new ArrayList<>();
        if (!priorityQueue.isEmpty()) {
            for (int i = 0; i < 15; i++) {
                Neighbor neighbor = priorityQueue.poll();
                Book recommendedBook = neighbor.getVector().getBook();
                Recommendation recommendation = new Recommendation(book, recommendedBook);
                recommendations.add(recommendation);
            }
        } 

        return recommendations;
    }
    
}
