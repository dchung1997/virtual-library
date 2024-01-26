package com.example.virtuallibrary.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.virtuallibrary.models.BookCheckout;

public interface BookCheckoutRepository extends CrudRepository<BookCheckout, Long>  {
    
}
