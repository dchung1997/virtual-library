package com.example.virtuallibrary.exceptions;

public class NullBookException extends RuntimeException {
    public NullBookException() {
        super();
    }

    public NullBookException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullBookException(String message) {
        super(message);
    }    

    public NullBookException(Throwable cause) {
        super(cause);
    }    
}
