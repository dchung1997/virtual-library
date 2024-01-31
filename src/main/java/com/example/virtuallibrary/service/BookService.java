package com.example.virtuallibrary.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.virtuallibrary.exceptions.BookAlreadyExistsException;
import com.example.virtuallibrary.exceptions.BookIdMismatchException;
import com.example.virtuallibrary.exceptions.BookNotFoundException;
import com.example.virtuallibrary.models.Book;
import com.example.virtuallibrary.models.BookCheckout;
import com.example.virtuallibrary.models.CategoriesCount;
import com.example.virtuallibrary.models.RatingInfo;
import com.example.virtuallibrary.models.Recommendation;
import com.example.virtuallibrary.models.User;
import com.example.virtuallibrary.repository.BookCheckoutRepository;
import com.example.virtuallibrary.repository.BookRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookCheckoutRepository bookCheckoutRepository;

    @Autowired
    private EntityManager em;

    public Page<Book> findAllBooks(Pageable pageable) {
      return bookRepository.findAll(pageable);
    }    

    public List<Book> findByTitle(String bookTitle) {
        return bookRepository.findByTitle(bookTitle);
    }

    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> findFifteenBooksRandom() {
      return bookRepository.findRandomBooks(PageRequest.of(0, 15));
    }    

    public List<Book> findFifteenBooksFiction() {
      return bookRepository.findFictionBooks(PageRequest.of(0, 15));
    }    

    public List<Book> findFifteenBooksNonFiction() {
      return bookRepository.findNonFictionBooks(PageRequest.of(0, 15));
    }

    public boolean checkout(Book book, User user) {
      int available_copies = book.getAvailable_copies();
      if (available_copies > 0) {
          List<BookCheckout> checkouts = user.getCheckouts() != null ? user.getCheckouts() : new ArrayList<BookCheckout>();
          
          boolean userHasCheckout = false;
          for (BookCheckout c : checkouts) {
            if (c.getBook().getId().equals(book.getId())) {
                userHasCheckout = true;
            }
          }

          if (!userHasCheckout) {
            available_copies = available_copies - 1;
            
            if (available_copies == 0 ) {
              book.setAvailable(false);
            }

            book.setAvailable_copies(available_copies);
            BookCheckout checkout = new BookCheckout(book, user);
            bookRepository.save(book);
            bookCheckoutRepository.save(checkout);
            return true;
          }
          // User has already checked out book.
          return false;
      }
      // No avialable copies of book.
      return false;
    }

    // public boolean returnBook(BookCheckout checkout) {
    //     if (checkouts.contains(checkout)) {
    //         available_copies++;
    //         checkouts.remove(checkout);
    //         return true;
    //     }
    //     return false; // Not a valid checkout record
    // }

    public List<Book> getBooksByRecommendations(List<Recommendation> recommendations) {
      List<Book> books = new ArrayList<>();
      for (Recommendation recommendation : recommendations) {
        books.add(recommendation.getRecommendedBook());
      }
      return books;
    }

    public Book createBook(Book book) {
        if (bookRepository.findById(book.getId()) != null) {
          throw new BookAlreadyExistsException();
        }
        return bookRepository.save(book);
    }

    public void deleteBook(String id) {
        if (bookRepository.findById(id) == null) {
          throw new BookNotFoundException();         
        }
        bookRepository.deleteById(id);
    }

    public Book updateBook(Book book, String id) {
        if (!book.getId().equals(id)) {
          throw new BookIdMismatchException();
        }

        if (bookRepository.findById(book.getId()) == null) {
          throw new BookNotFoundException();         
        }        

        return bookRepository.save(book);
    }

    public Page<Book> findByCriteria(String context, String criteria, String sort, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        Predicate rootPredicates = createPredicates(cb, root, context, criteria);

        if (rootPredicates != null) {
          query.where(rootPredicates);
        }

        if (sort != null) {
          switch (sort) {
              case "title", "author" -> query.orderBy(cb.asc(root.get(sort)));
              case "published_year", "average_rating" -> query.orderBy(cb.desc(root.get(sort)));
          }
        }
        
        query.distinct(true);

        TypedQuery<Book> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Book> books = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Book> countRoot = countQuery.from(Book.class);
        Predicate countRootPredicates = createPredicates(cb, countRoot, context, criteria);

        if (countRootPredicates != null) {
          countQuery.where(countRootPredicates);
        }

        countQuery.select(cb.count(countRoot));
        countQuery.distinct(true);        
        long totalElements = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(books, pageable, totalElements);
    }

    public List<CategoriesCount> getCategoryCount(String context) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CategoriesCount> query = cb.createQuery(CategoriesCount.class);

        Root<Book> root = query.from(Book.class);
        Predicate rootPredicates = createPredicates(cb, root, context, null);

        if (rootPredicates != null) {
          query.where(rootPredicates);
        }

        query.select(cb.construct(CategoriesCount.class, root.get("categories"), cb.count(root)));
        query.groupBy(root.get("categories"));
        query.orderBy(cb.desc(cb.count(root)));

        // Execute query and get results
        TypedQuery<CategoriesCount> typedQuery = em.createQuery(query);
        List<CategoriesCount> categoriesCount = typedQuery.getResultList();
        
        categoriesCount.removeIf(cc -> cc.getCount() < 5);
        categoriesCount.removeIf((cc -> cc.getCategories().isBlank()));

        return categoriesCount;
    }

    private Predicate createPredicates(CriteriaBuilder cb, Root<?> root, String context, String criteria) {
      List<Predicate> predicatesContext = new ArrayList<>();
      List<Predicate> predicatesCriteria = new ArrayList<>();
  
      if (context != null) {
        String[] propertyNames = {"categories", "isbn", "author", "title"};
        
        for (String propertyName : propertyNames) {
          predicatesContext.add(cb.like(cb.lower(root.get(propertyName)), "%" + context.toLowerCase() + "%"));
        }
      }

      if (criteria != null) {
        String[] delimitedCategories = criteria.split(",");
        
        for (String category : delimitedCategories) {
          Predicate categoryPredicate = cb.equal(root.get("categories"), category);
          predicatesCriteria.add(categoryPredicate);          
        }
      }
      

      Predicate combinedPredicateContext = cb.or(predicatesContext.toArray(new Predicate[0]));
      Predicate combinedPredicateCriteria = cb.or(predicatesCriteria.toArray(new Predicate[0]));
      
      int contextSize = predicatesContext.size();
      int criteriaSize = predicatesCriteria.size();

      if (contextSize > 0 && criteriaSize > 0) {
        return cb.and(combinedPredicateContext, combinedPredicateCriteria);
      } else if (contextSize > 0) {
        return combinedPredicateContext;
      } else if (criteriaSize > 0) {
        return combinedPredicateCriteria;
      } else {
        return null;
      }
    }    

  public RatingInfo calculateRatingInfo(Book book) {
      double rating = book.getAverage_rating();
      int wholeStars = (int) rating;
      double fractionalPart = rating - wholeStars;
      return new RatingInfo(wholeStars, fractionalPart);
  }    


}
