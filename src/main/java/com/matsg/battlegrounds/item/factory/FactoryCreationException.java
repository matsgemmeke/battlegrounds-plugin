package com.matsg.battlegrounds.item.factory;

public class FactoryCreationException extends RuntimeException {

    public FactoryCreationException() {
        super();
    }

    public FactoryCreationException(String message) {
        super(message);
    }

    public FactoryCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
