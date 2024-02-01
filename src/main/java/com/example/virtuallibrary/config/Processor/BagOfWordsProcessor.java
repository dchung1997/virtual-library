package com.example.virtuallibrary.config.Processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.virtuallibrary.models.BagOfWords;
import com.example.virtuallibrary.models.Book;

@Component
public class BagOfWordsProcessor implements ItemProcessor<Book,  BagOfWords> {

    public BagOfWordsProcessor() {
        super();
    }

    @Override
    public BagOfWords process(Book item) throws Exception {
        // 1. Normalize Text
        String normalizedDescription = Arrays.stream(item.getDescription().toLowerCase().split("\\s+"))
            .filter(word -> word.matches("[a-zA-Z0-9]+"))
            .collect(Collectors.joining(" "));

        // 2. Tokenize into words
        String[] words = normalizedDescription.split("\\s+");

        // 3. Remove stop words (assuming you have a list of stop words)
        Set<String> stopWords = new HashSet<>(Arrays.asList( "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with")); // Add more stop words

        HashMap<String, Double> bagOfWords = new HashMap<>();

        for (String word : words) {
            Double count = bagOfWords.get(word);
            if (!stopWords.contains(word)) {
                if (count == null) {
                    count = 0.0;
                }
                bagOfWords.put(word, count + 1);
            }
        }

        BagOfWords bow = new BagOfWords(bagOfWords, item);
        return bow; // Return the created BoW vector
    }
    
}
