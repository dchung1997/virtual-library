package com.example.virtuallibrary.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.BindException;

import com.example.virtuallibrary.config.Listener.bookDataSkipListener;
import com.example.virtuallibrary.models.Book;

@Configuration
public class DatabaseJobConfiguration {
    
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
    
    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("DatabaseJobConfiguration6", jobRepository)
        .start(step)
        .build();
    }    
    @Bean 
    public Step step(JobRepository jobRepository, JdbcTransactionManager transactionManager, 
                        ItemReader<Book> bookDataFileReader, ItemWriter<Book> bookDataTableWriter, bookDataSkipListener skipListener) {
        return new StepBuilder("fileIngesting", jobRepository)
                .<Book,Book>chunk(100, transactionManager)
                .reader(bookDataFileReader)
                .writer(bookDataTableWriter)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skipLimit(100)
                .listener(skipListener)
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
                            book.setIsbn(fieldSet.readString("isbn"));
                            book.setTitle(fieldSet.readString("title"));
                            book.setAuthor(fieldSet.readString("authors"));
                            book.setCategories(fieldSet.readString("categories"));
                            book.setThumbnail(fieldSet.readString("thumbnail"));
                            book.setDescription(fieldSet.readString("description"));
                            book.setPublished_year(fieldSet.readInt("published_year", -1));
                            book.setNum_pages(fieldSet.readInt("num_pages", -1));
                            book.setRatings_count(fieldSet.readInt("ratings_count", -1));
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
    public JdbcBatchItemWriter<Book> billingDataTableWriter(DataSource dataSource) {
        String sql = "insert into BOOK(isbn, title, author, categories, thumbnail, description, published_year, average_rating, num_pages, ratings_count) " +  
                        "values(:isbn, :title, :author, :categories, :thumbnail, :description, :published_year, :average_rating, :num_pages, :ratings_count)";

        return new JdbcBatchItemWriterBuilder<Book>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }
    
    @Bean
    public bookDataSkipListener skipListener() {
        return new bookDataSkipListener();
    }
    
}
