package com.example.virtuallibrary.config.Writer;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.virtuallibrary.models.BagOfWords;
import com.example.virtuallibrary.models.WordVector;

public class BagOfWordsItemWriter implements ItemWriter<BagOfWords> {
    private final JdbcTemplate jdbcTemplate;

    public BagOfWordsItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(Chunk<? extends BagOfWords> chunk) throws Exception {
        List<Object[]> batchArgs = new ArrayList<>();
        for (BagOfWords bow : chunk) {
            WordVector wv = new WordVector(bow.getVector());
            String[] vector = wv.getWords();
            Double[] counts = wv.getCounts();
            batchArgs.add(new Object[]{vector, counts, bow.getBook().getId()});
        }
        jdbcTemplate.batchUpdate("insert into WORD_VECTOR(words, counts, book_id) VALUES (?, ?, ?)", batchArgs);
    }
    
}
