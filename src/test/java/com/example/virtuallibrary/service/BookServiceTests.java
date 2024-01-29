package com.example.virtuallibrary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.virtuallibrary.exceptions.BookAlreadyExistsException;
import com.example.virtuallibrary.exceptions.BookIdMismatchException;
import com.example.virtuallibrary.exceptions.BookNotFoundException;
import com.example.virtuallibrary.models.Book;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class BookServiceTests {
    @Autowired
    private BookService bookService;

    @Test
    public void testCreateBook() {
        // Create a book object
        Book book = new Book("978000200588T", "Gilead", "Marilynne Robinson", "Fiction", "", "",
        2004, 3.85, 247, 361, 3, 3, null);

        // Call the createBook method
        Book createdBook = bookService.createBook(book);
        assertEquals(book.getIsbn(), createdBook.getIsbn());
    }

    @Test
    public void testDuplicateBook() {
        // Create a book object
        Book book = new Book("978000200588T", "Gilead", "Marilynne Robinson", "Fiction", "", "",
        2004, 3.85, 247, 361, 3, 3, null);

        // Call the createBook method
        Book createdBook = bookService.createBook(book);
        assertEquals(book.getIsbn(), createdBook.getIsbn());
        
        assertThrows(BookAlreadyExistsException.class, () -> {
            bookService.createBook(book);
        });
    }

    @Test
    public void testDeleteBook() {
        // Create a book object
        Book book = new Book("978000200588T", "Gilead", "Marilynne Robinson", "Fiction", "", "",
        2004, 3.85, 247, 361, 3, 3, null);

        // Call the createBook method
        Book createdBook = bookService.createBook(book);
        assertEquals(book.getIsbn(), createdBook.getIsbn());
    
        bookService.deleteBook(book.getIsbn());
        assertNull(bookService.findByIsbn(book.getIsbn()));
    }

    @Test
    public void testEmptyDeleteBook() {
        // Create a book object
        Book book = new Book("978000200588T", "Gilead", "Marilynne Robinson", "Fiction", "", "",
        2004, 3.85, 247, 361, 3, 3, null);

        assertThrows(BookNotFoundException.class, () -> {
            bookService.deleteBook(book.getIsbn());
        });
    }

    @Test
    public void testUpdateBook() {
        // Create a book object
        Book book = new Book("978000200588T", "Gilead", "Marilynne Robinson", "Fiction", "", "",
        2004, 3.85, 247, 361, 3, 3, null);

        // Call the createBook method
        Book createdBook = bookService.createBook(book);
        assertEquals(book.getIsbn(), createdBook.getIsbn());

        createdBook.setDescription("A NOVEL THAT READERS and critics have been eagerly anticipating for over a decade, Gilead is an astonishingly imagined story of remarkable lives. John Ames is a preacher, the son of a preacher and the grandson (both maternal and paternal) of preachers. It’s 1956 in Gilead, Iowa, towards the end of the Reverend Ames’s life, and he is absorbed in recording his family’s story, a legacy for the young son he will never see grow up. Haunted by his grandfather’s presence, John tells of the rift between his grandfather and his father: the elder, an angry visionary who fought for the abolitionist cause, and his son, an ardent pacifist. He is troubled, too, by his prodigal namesake, Jack (John Ames) Boughton, his best friend’s lost son who returns to Gilead searching for forgiveness and redemption. Told in John Ames’s joyous, rambling voice that finds beauty, humour and truth in the smallest of life’s details, Gilead is a song of celebration and acceptance of the best and the worst the world has to offer. At its heart is a tale of the sacred bonds between fathers and sons, pitch-perfect in style and story, set to dazzle critics and readers alike.");
        createdBook.setThumbnail("http://books.google.com/books/content?id=KQZCPgAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");

        Book updatedBook = bookService.updateBook(createdBook, createdBook.getIsbn());
        assertEquals(createdBook.getDescription(), updatedBook.getDescription());
        assertEquals(createdBook.getThumbnail(), updatedBook.getThumbnail());
    
        assertNotEquals(book.getDescription(), updatedBook.getDescription());
        assertNotEquals(book.getThumbnail(), updatedBook.getThumbnail());
    }  

    @Test
    public void testDeadUpdateBook() {
        // Create a book object
        Book book = new Book("978000200588T", "Gilead", "Marilynne Robinson", "Fiction", "", "",
        2004, 3.85, 247, 361, 3, 3, null);

        assertThrows(BookNotFoundException.class, () -> {
            bookService.updateBook(book, "1780002005883");
        });
    }  

    @Test
    public void testMalformatedUpdateBook() {
        // Create a book object
        Book book = new Book("978000200588T", "Gilead", "Marilynne Robinson", "Fiction", "", "",
        2004, 3.85, 247, 361, 3, 3, null);

        // Call the createBook method
        Book createdBook = bookService.createBook(book);
        assertEquals(book.getIsbn(), createdBook.getIsbn());

        createdBook.setDescription("A NOVEL THAT READERS and critics have been eagerly anticipating for over a decade, Gilead is an astonishingly imagined story of remarkable lives. John Ames is a preacher, the son of a preacher and the grandson (both maternal and paternal) of preachers. It’s 1956 in Gilead, Iowa, towards the end of the Reverend Ames’s life, and he is absorbed in recording his family’s story, a legacy for the young son he will never see grow up. Haunted by his grandfather’s presence, John tells of the rift between his grandfather and his father: the elder, an angry visionary who fought for the abolitionist cause, and his son, an ardent pacifist. He is troubled, too, by his prodigal namesake, Jack (John Ames) Boughton, his best friend’s lost son who returns to Gilead searching for forgiveness and redemption. Told in John Ames’s joyous, rambling voice that finds beauty, humour and truth in the smallest of life’s details, Gilead is a song of celebration and acceptance of the best and the worst the world has to offer. At its heart is a tale of the sacred bonds between fathers and sons, pitch-perfect in style and story, set to dazzle critics and readers alike.");
        createdBook.setThumbnail("http://books.google.com/books/content?id=KQZCPgAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");

        assertThrows(BookIdMismatchException.class, () -> {
            bookService.updateBook(createdBook, "1780002005883");
        });
    }      

    @Test
    public void testFindFifteenBooksRandom() {
         List<Book> randomBooks = bookService.findFifteenBooksRandom();
         assertEquals(randomBooks.size(), 15);
    }

    @Test
    public void testFindFifteenBooksFiction() {
         List<Book> fictionBooks = bookService.findFifteenBooksFiction();
         assertEquals(fictionBooks.size(), 15);
         for (Book b : fictionBooks) {
            assertEquals(b.getCategories(), "Fiction");
         }
    }

    @Test
    public void testFindFifteenBooksNonFiction() {
         List<Book> nonFictionBooks = bookService.findFifteenBooksNonFiction();
         String[] possibleCategories = new String[]{"Nonfiction", "Business", "Self-Help", "Art", "Philosophy", "Psychology", "Sports", "Poetry", "History"};
         assertEquals(nonFictionBooks.size(), 15);
         for (Book b : nonFictionBooks) {
            // Check if any of the possible categories match the book's category
            boolean hasMatchingCategory = Arrays.stream(possibleCategories)
                                            .anyMatch(b.getCategories()::contains);

            assertTrue(hasMatchingCategory, "Book should have a matching category");
         }
    }
}
