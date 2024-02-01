package com.example.virtuallibrary.exceptions;

public class BookAlreadyCheckedOutException extends RuntimeException {
    public BookAlreadyCheckedOutException() {
        super();
    }

    public BookAlreadyCheckedOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookAlreadyCheckedOutException(String message) {
        super(message);
    }    

    public BookAlreadyCheckedOutException(Throwable cause) {
        super(cause);
    }    
}