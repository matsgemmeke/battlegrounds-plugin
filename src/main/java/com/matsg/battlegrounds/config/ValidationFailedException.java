package com.matsg.battlegrounds.config;

public class ValidationFailedException extends Exception {

    public ValidationFailedException() {
        super();
    }

    public ValidationFailedException(String message) {
        super(message);
    }

    public ValidationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}