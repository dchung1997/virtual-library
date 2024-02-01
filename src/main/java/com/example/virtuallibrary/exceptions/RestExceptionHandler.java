package com.example.virtuallibrary.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


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

    @ExceptionHandler({ BookAlreadyExistsException.class })
    protected ResponseEntity<Object> handleBookAlreadyExists(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "Book already exists.", new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ BookAlreadyCheckedOutException.class })
    protected ResponseEntity<Object> handleBookAlreadyCheckedOut(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "You have already checked out this book.", new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ BookUnavailableException.class })
    protected ResponseEntity<Object> handleBookUnavailable(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "The book you are trying to check out is unavailable.", new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }    

    @ExceptionHandler({ NullBookException.class })
    protected ResponseEntity<Object> handleNullBook(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "The book you were looking for does not exist.", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }    


    @ExceptionHandler({ UserAlreadyExistsException.class })
    protected ResponseEntity<Object> handleUserAlreadyExists(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "User already exists.", new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
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