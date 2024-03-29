package com.example.virtuallibrary.exceptions;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException() {
        super();
    }

    public BookAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookAlreadyExistsException(String message) {
        super(message);
    }    

    public BookAlreadyExistsException(Throwable cause) {
        super(cause);
    }    
}
