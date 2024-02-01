package com.example.virtuallibrary.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.validation.BindException;

import com.example.virtuallibrary.config.Listener.bookDataSkipListener;
import com.example.virtuallibrary.config.Processor.BagOfWordsProcessor;
import com.example.virtuallibrary.config.Processor.BookRecommendationProcessor;
import com.example.virtuallibrary.config.Writer.BagOfWordsItemWriter;
import com.example.virtuallibrary.config.Writer.RecommendationItemWriter;
import com.example.virtuallibrary.models.BagOfWords;
import com.example.virtuallibrary.models.Book;
import com.example.virtuallibrary.models.Recommendation;
import com.example.virtuallibrary.models.WordVector;

@Configuration
@ComponentScan("com.example.virtuallibrary")
public class DatabaseJobConfiguration {
    
    @Bean
    public Job job(JobRepository jobRepository, Step step1, Step step2, Step step3) {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new JobBuilder("BookDatabaseJobConfiguration", jobRepository)
        .start(step1)
        .next(step2)
        .next(step3)
        .build();
    }    

    @Bean 
    public Step step1(JobRepository jobRepository, JpaTransactionManager transactionManager, 
                        FlatFileItemReader<Book> bookDataFileReader, JdbcBatchItemWriter<Book> bookDataTableWriter, bookDataSkipListener skipListener) {
        return new StepBuilder("fileIngesting", jobRepository)
                .<Book, Book>chunk(100, transactionManager)
                .reader(bookDataFileReader)
                .writer(bookDataTableWriter)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skipLimit(100)
                .listener(skipListener)
                .build();
    }   

    // We want to process the bag of words to write to table.
    @Bean 
    public Step step2(JobRepository jobRepository, JpaTransactionManager transactionManager, 
                        @Qualifier("bookDataTableReader") JdbcCursorItemReader<Book> bookTableReader, ItemProcessor<Book, 
                        BagOfWords> bagOfWordsProcessor, ItemWriter<BagOfWords> bagOfWordsItemWriter) {
        return new StepBuilder("bagOfWordsGeneration", jobRepository)
                .<Book,BagOfWords>chunk(100, transactionManager)
                .reader(bookTableReader)
                .processor(bagOfWordsProcessor)
                .writer(bagOfWordsItemWriter)
                .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository, JpaTransactionManager transactionManager, 
                        @Qualifier("bookVectorDataTableReader") JdbcCursorItemReader<Book> bookTableReader, ItemProcessor<Book, 
                        List<Recommendation>> bookRecommendationProcessor, ItemWriter<List<Recommendation>> recommendationItemWriter) {
        return new StepBuilder("bookRecommendation", jobRepository)
            .<Book,List<Recommendation>>chunk(100, transactionManager)
            .reader(bookTableReader)
            .processor(bookRecommendationProcessor)
            .writer(recommendationItemWriter)
            .build();
    }
    
    @Bean
    public FlatFileItemReader<Book> bookDataFileReader() {
        DefaultLineMapper<Book> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames("isbn", "title", "authors", "categories", "thumbnail", 
                                    "description", "published_year", "average_rating", "num_pages", "ratings_count"); // Select desired columns
                        setIncludedFields(0, 2, 4, 5, 6, 7, 8, 9, 10, 11);                                    
        }});

        lineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<Book>() {
                        @Override
                        public Book mapFieldSet(FieldSet fieldSet) throws BindException {
                            Book book = new Book();
                            Random random = new Random();
                            int copies = random.nextInt(6) + 1;
                            book.setIsbn(fieldSet.readString("isbn"));
                            book.setTitle(fieldSet.readString("title"));
                            book.setAuthor(fieldSet.readString("authors"));
                            book.setCategories(fieldSet.readString("categories"));
                            book.setThumbnail(fieldSet.readString("thumbnail"));
                            book.setDescription(fieldSet.readString("description"));
                            book.setPublished_year(fieldSet.readInt("published_year", -1));
                            book.setNum_pages(fieldSet.readInt("num_pages", -1));
                            book.setRatings_count(fieldSet.readInt("ratings_count", -1));
                            book.setAvailable(true);
                            book.setAvailable_copies(copies);
                            book.setTotal_copies(copies);
                            try {
                                book.setAverage_rating(fieldSet.readDouble("average_rating"));
                            } catch (NullPointerException ex) {
                                book.setAverage_rating(-1);
                            }

                            return book;                            
                        }
        });

        return new FlatFileItemReaderBuilder<Book>()
                .name("bookDataFileReader")
                .resource(new FileSystemResource("src/main/resources/data/books.csv"))
                .lineMapper(lineMapper)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Book> bookDataTableWriter(DataSource dataSource) {
        String sql = "insert into BOOK(isbn, title, author, categories, thumbnail, description, published_year, average_rating, num_pages, ratings_count, available, available_copies, total_copies) " +  
                        "values(:isbn, :title, :author, :categories, :thumbnail, :description, :published_year, :average_rating, :num_pages, :ratings_count, :available, :available_copies, :total_copies)";

        return new JdbcBatchItemWriterBuilder<Book>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }

    @Bean
    @Qualifier("bookDataTableReader")
    public JdbcCursorItemReader<Book> bookDataTableReader(DataSource dataSource) {
        String sql = "SELECT  * FROM book";

        return new JdbcCursorItemReaderBuilder<Book>()
                .name("bookDataTableReader")
                .dataSource(dataSource)
                .sql(sql)
                .rowMapper(new DataClassRowMapper<Book>(Book.class))
                .build();
    }

    @Bean
    @Qualifier("bookVectorDataTableReader")
    public JdbcCursorItemReader<Book> bookVectorDataTableReader(DataSource dataSource) {
        String sql = "SELECT * FROM book LEFT JOIN word_vector ON book.isbn = word_vector.book_id";

        return new JdbcCursorItemReaderBuilder<Book>()
                .name("bookDataTableReader")
                .dataSource(dataSource)
                .sql(sql)
                .rowMapper((rs, rowNum) -> new Book(
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("categories"),
                    rs.getString("thumbnail"),
                    rs.getString("description"),
                    rs.getInt("published_year"),
                    rs.getDouble("average_rating"),
                    rs.getInt("num_pages"),
                    rs.getInt("ratings_count"),
                    rs.getInt("available_copies"),
                    rs.getInt("total_copies"),
                    new WordVector(rs.getLong("id"), rs.getArray("words"), rs.getArray("counts"))
                ))
                .build();
    }

    @Bean
    public ItemProcessor<Book, BagOfWords> BagOfWordsProcessorProcessor() {
        return new BagOfWordsProcessor();
    }


    @Bean
    public ItemWriter<BagOfWords> BagOfWordsItemWriter(DataSource dataSource) {
        return new BagOfWordsItemWriter(dataSource);
    }
    

    @Bean
    public ItemProcessor<Book, List<Recommendation>> bookRecommendationProcessor() {
        return new BookRecommendationProcessor();
    }


    @Bean
    public ItemWriter<List<Recommendation>> RecommendationItemWriter(DataSource dataSource) {
        return new RecommendationItemWriter(dataSource);
    }

    @Bean
    public bookDataSkipListener skipListener() {
        return new bookDataSkipListener();
    }
    
}
