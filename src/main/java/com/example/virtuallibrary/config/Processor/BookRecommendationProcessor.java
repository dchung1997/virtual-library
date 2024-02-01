package com.example.virtuallibrary.config.Processor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.text.similarity.JaroWinklerDistance;
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
    private boolean init;
    private double max;

    // I want to store in memory values from bookRepository for continued use.
    public BookRecommendationProcessor() {
        super();
        this.init = true;
        this.vectors = null;
        this.max = -1;
    }

    @Override
    // Implements a K-NN Approach using Bag of Words.
    public List<Recommendation> process(Book book) {
        // Lazy initialization.
        if (this.vectors == null) {
            this.vectors = wordVectorRepository.findAll();
            
        }

        if (this.max == -1) {
            this.max = 0;
            for (WordVector vector : vectors) {
                Double[] counts = vector.getCounts();
                for (int i = 0; i < counts.length; i++) {
                    if (max < counts[i]) {
                        max = counts[i];
                    }
                }
            }
        }

        if (this.init) {
            // Iterate through vectors and normalize.
            for (WordVector vector : vectors) {
                Double[] counts = vector.getCounts();
                for (int i = 0; i < counts.length; i++) {
                        counts[i] = counts[i] / max;
                }
            }
            this.init = false;       
        }     

        WordVector bag = book.getBagOfWords();
        List<Double> normCounts = new ArrayList<Double>();
        for (Double count : bag.getCounts()) {
            normCounts.add(count / this.max);
        }
        
        PriorityQueue<Neighbor> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(Neighbor::getDistance));
        // Compare distance between vectors.
        for (WordVector vector : vectors) {
            if (bag.getId() != vector.getId()) {
                double total = 0;
                double distance = bag.euclideanDistance(vector, max);
                if (distance != 0) {
                    total += distance * 0.4;
                }
        
                // Check if categories are the same or similar and same author.
                String categories = book.getCategories();
                String author = book.getAuthor();
                String[] authors = author.split(";");
                
                Book vectorBook = vector.getBook();
                String vectorCategories = vectorBook.getCategories();
                String vectorAuthor = vectorBook.getAuthor();
                String[] vectorAuthors = vectorAuthor.split(";");           

                boolean authorsSimilar = true;
                JaroWinklerDistance jwDistance = new JaroWinklerDistance();
                for (int c = 0; c < authors.length; c++) {
                    for (int r = 0; r < vectorAuthors.length; r++) {
                        double authorSimilarity = jwDistance.apply(authors[c], vectorAuthors[r]);
                        authorsSimilar &= authorSimilarity <= 0.3; 
                    }
                }

                if (authorsSimilar){
                    total += 0.3;
                }

                double categoryDistance = jwDistance.apply(vectorCategories, categories);
                total += categoryDistance * 0.3;


                priorityQueue.add(new Neighbor(vector, total));

            }
        }

        // Generates a list of recommendations.
        List<Recommendation> recommendations = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Neighbor neighbor = priorityQueue.poll();
            Book recommendedBook = neighbor.getVector().getBook();
            Recommendation recommendation = new Recommendation(book, recommendedBook);
            recommendations.add(recommendation);
        } 

        return recommendations;
    }
    
}
