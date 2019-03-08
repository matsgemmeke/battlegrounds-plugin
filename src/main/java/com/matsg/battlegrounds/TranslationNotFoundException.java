package com.matsg.battlegrounds;

public class TranslationNotFoundException extends RuntimeException {

    public TranslationNotFoundException() {
        super();
    }

    public TranslationNotFoundException(String message) {
        super(message);
    }

    public TranslationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
