package com.example.virtuallibrary.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.virtuallibrary.exceptions.BookIdMismatchException;
import com.example.virtuallibrary.exceptions.BookNotFoundException;
import com.example.virtuallibrary.exceptions.UserIdMismatchException;
import com.example.virtuallibrary.exceptions.UserNotFoundException;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public RestExceptionHandler() {
        super();
    }


    @ExceptionHandler({ BookNotFoundException.class })
    protected ResponseEntity<Object> handleBookNotFound(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "Book not found", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ UserNotFoundException.class })
    protected ResponseEntity<Object> handleUserNotFound(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "User not found", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ BookIdMismatchException.class, UserIdMismatchException.class, ConstraintViolationException.class, DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}