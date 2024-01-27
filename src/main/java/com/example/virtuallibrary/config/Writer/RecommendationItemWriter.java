package com.example.virtuallibrary.config.Writer;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.virtuallibrary.models.Recommendation;

public class RecommendationItemWriter implements ItemWriter<List<Recommendation>> {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(Chunk<? extends List<Recommendation>> chunk) throws Exception {
        List<Object[]> batchArgs = new ArrayList<>();
        for (List<Recommendation> recommendedList : chunk) {
            for (Recommendation recommendation : recommendedList) {
                batchArgs.add(new Object[]{recommendation.getBook().getId(), recommendation.getRecommendedBook().getId()});
            }
        }
        jdbcTemplate.batchUpdate("insert into RECOMMENDATION(book_id, recommended_book_id) VALUES (?, ?)", batchArgs);
    }
    
}
