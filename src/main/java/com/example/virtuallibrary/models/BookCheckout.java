package com.example.virtuallibrary.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BookCheckout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDate checkoutDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)    
    private Boolean returned;

    public BookCheckout() {
        super();
    }

    public BookCheckout(Book book, User user) {
        this.book = book;
        this.user = user;
        this.checkoutDate = LocalDate.now();
        this.dueDate = checkoutDate.plusDays(14); // Set default due date as 7 days from checkout
        this.returned = false;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean isReturned() {
        return returned;
    }

    public void markReturned() {
        this.returned = true;
    }

    @Override
    public String toString() {
        return "BookCheckout{" +
                "book=" + book +
                ", user=" + user +
                ", checkoutDate=" + checkoutDate +
                ", dueDate=" + dueDate +
                ", returned=" + returned +
                '}';
    }
}