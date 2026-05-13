package com.killeen.taskflow.components.email.exception;

public class InvalidEmailTokenException extends RuntimeException {

    public InvalidEmailTokenException(String message) {
        super(message);
    }
    
}
