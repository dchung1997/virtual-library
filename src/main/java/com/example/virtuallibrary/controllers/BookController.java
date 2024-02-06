package com.example.virtuallibrary.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.virtuallibrary.exceptions.BookUnavailableException;
import com.example.virtuallibrary.exceptions.BookAlreadyCheckedOutException;
import com.example.virtuallibrary.exceptions.NullBookException;
import com.example.virtuallibrary.models.Book;
import com.example.virtuallibrary.models.CategoriesCount;
import com.example.virtuallibrary.models.RatingInfo;
import com.example.virtuallibrary.models.User;
import com.example.virtuallibrary.service.BookService;
import com.example.virtuallibrary.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @GetMapping
    public ModelAndView findAll(Pageable pageable) {
        return new ModelAndView("redirect:/books/browse");
    }

    @GetMapping("/browse")
    public ModelAndView browseAndSearch(@RequestParam(required = false) String context, @RequestParam(required = false) String criteria, @RequestParam(required = false) String sort, Pageable pageable) {
        ModelAndView books = new ModelAndView("browse");
        Page<Book> queryBooks = bookService.findByCriteria(context, criteria, sort, pageable);
        List<CategoriesCount> categoryCount = bookService.getCategoryCount(context);
        String[] delimitedCategories = criteria != null ? criteria.split(",") : new String[]{} ;
        String sorter = sort != null ? sort : "Relevance";

        books.addObject("categories", categoryCount);
        books.addObject("results", queryBooks);
        books.addObject("sort", sorter);
        books.addObject("context", context);
        books.addObject("criteria", criteria);
        books.addObject("delimitedCategories", delimitedCategories);
        books.addObject("page", pageable.getPageNumber());
        books.addObject("pageItems", queryBooks.getNumberOfElements());
        books.addObject("totalElements", queryBooks.getTotalElements());
        books.addObject("totalPages", queryBooks.getTotalPages());        
        return books;
    }

    @GetMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView checkout(HttpSession session) {
        ModelAndView checkout = new ModelAndView("checkout");

        List<Book> cart = (List<Book>) session.getAttribute("cart");
        boolean available = true;

        if (cart != null) {
            for (int i = 0; i < cart.size(); i++) {
                if (!cart.get(i).isAvailable()) {
                    available = false;
                }
            }
        } else {
            available = false;
        }


        checkout.addObject("cart", cart);
        checkout.addObject("available", available);

        return checkout;
    }    

    @GetMapping("/checkout/hold")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView checkoutBooks(HttpSession session, RedirectAttributes redirectAttributes, Authentication authentication) {
        ModelAndView checkout = new ModelAndView("redirect:/home");

        List<Book> cart = (List<Book>) session.getAttribute("cart");
        List<Book> removalArray = new ArrayList<>();
        User user = userDetailsService.findByUserName(authentication.getName());

        for (Book book : cart) {
            if (book.isAvailable()) {
                bookService.checkout(book, user);
                removalArray.add(book);
            } 
        }

        cart.removeAll(removalArray);
        session.setAttribute("cart", cart);

        if (cart.size() > 0) {
            ModelAndView error = new ModelAndView("redirect:/books/checkout");
            redirectAttributes.addFlashAttribute("message", "Unable to checkout the following books.");
            return error;
        }

        redirectAttributes.addFlashAttribute("message", "You have successfully checked out all books.");
        return checkout;
    }    


    @GetMapping("/{isbn}")  
    public ModelAndView getBook(@PathVariable String isbn, RedirectAttributes redirectAttributes) {
        ModelAndView bookView = new ModelAndView("book");
        Book book = bookService.findByIsbn(isbn);
        
        if (book == null) {
            ModelAndView error = new ModelAndView("redirect:/home");
            redirectAttributes.addFlashAttribute("message", "The book you were looking for does not exist.");
            return error;
        }

        int totalBooks = book.getTotal_copies();
        int available = book.getAvailable_copies();
        RatingInfo ratingInfo = bookService.calculateRatingInfo(book);
        
        List<Book> recommendationsList = bookService.getBooksByRecommendations(book.getRecommendations());

        bookView.addObject("rating", ratingInfo.getWholeStars());
        bookView.addObject("fractionalPart", ratingInfo.getFractionalPart());
        bookView.addObject("available", available);
        bookView.addObject("copies", totalBooks);
        bookView.addObject("book", book);
        bookView.addObject("recommendationsList5", recommendationsList.subList(0, 5));
        bookView.addObject("recommendationsList10", recommendationsList.subList(5, 10));
        bookView.addObject("recommendationsList15", recommendationsList.subList(10, 15));

        return bookView;
    } 
    
    @GetMapping("/{isbn}/hold") 
    @PreAuthorize("isAuthenticated()")
    public ModelAndView holdBook(@PathVariable String isbn, RedirectAttributes redirectAttributes, Authentication authentication) {
        ModelAndView bookView = new ModelAndView("redirect:/books/" + isbn);

        User user = userDetailsService.findByUserName(authentication.getName());
        Book book = bookService.findByIsbn(isbn);

        try {
            bookService.checkout(book, user);
        } catch (NullBookException nbEx) {
            ModelAndView error = new ModelAndView("redirect:/home");
            redirectAttributes.addFlashAttribute("message", nbEx.getMessage());
            return error;
        } catch (BookUnavailableException buEx) {
            redirectAttributes.addFlashAttribute("message", buEx.getMessage());
            redirectAttributes.addFlashAttribute("error", "unavailable");
            return bookView;
            
        } catch (BookAlreadyCheckedOutException baeEx) {
            redirectAttributes.addFlashAttribute("message", baeEx.getMessage());
            redirectAttributes.addFlashAttribute("error", "unavailable");
            return bookView;
        }

        redirectAttributes.addFlashAttribute("message", "You have successfully checked out " + book.getTitle() + ".");        
        return bookView;
    }    

    @GetMapping("/{isbn}/save")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView saveBook(@PathVariable String isbn, RedirectAttributes redirectAttributes, HttpSession session, Authentication authentication) {
        ModelAndView bookView = new ModelAndView("redirect:/books/" + isbn);

        User user = userDetailsService.findByUserName(authentication.getName());
        Book book = bookService.findByIsbn(isbn);
        List<Book> cart = (List<Book>) session.getAttribute("cart"); 
        
        if (cart == null) {
            cart = new ArrayList<>();
        }

        try {
            cart = bookService.addToCart(book, cart, user);
            redirectAttributes.addFlashAttribute("message", "You have successfully saved " + book.getTitle() + ".");
            session.setAttribute("cart", cart);
        } catch (NullBookException nbEx) {
            ModelAndView error = new ModelAndView("redirect:/home");
            redirectAttributes.addFlashAttribute("message", nbEx.getMessage());
            return error;
        } catch (BookUnavailableException buEx) {
            redirectAttributes.addFlashAttribute("message", buEx.getMessage());
            redirectAttributes.addFlashAttribute("error", "unavailable");
            return bookView;
        } catch (BookAlreadyCheckedOutException bacoEx) {
            redirectAttributes.addFlashAttribute("message", bacoEx.getMessage());
            redirectAttributes.addFlashAttribute("error", "unavailable");
            return bookView;
        }

        return bookView;
    }    

    @GetMapping("/{isbn}/return")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView returnBook(@PathVariable String isbn, RedirectAttributes redirectAttributes, Authentication authentication) {
        ModelAndView bookView = new ModelAndView("redirect:/books/" + isbn);

        User user = userDetailsService.findByUserName(authentication.getName());
        Book book = bookService.findByIsbn(isbn);

        try {
            bookService.returnBook(book, user);
            redirectAttributes.addFlashAttribute("message", "You have successfully returned " + book.getTitle() + ".");
        } catch (NullBookException nbEx) {
            ModelAndView error = new ModelAndView("redirect:/home");
            redirectAttributes.addFlashAttribute("message", nbEx.getMessage());
            return error;
        } catch (BookUnavailableException buEx) {
            redirectAttributes.addFlashAttribute("message", buEx.getMessage());
            redirectAttributes.addFlashAttribute("error", "unavailable");
            return bookView;
        }

        return bookView;
    }
    

}
