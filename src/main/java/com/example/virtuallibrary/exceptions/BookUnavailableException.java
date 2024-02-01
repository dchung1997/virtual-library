package com.example.virtuallibrary.exceptions;

public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException() {
        super();
    }

    public BookUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookUnavailableException(String message) {
        super(message);
    }    

    public BookUnavailableException(Throwable cause) {
        super(cause);
    }    
}