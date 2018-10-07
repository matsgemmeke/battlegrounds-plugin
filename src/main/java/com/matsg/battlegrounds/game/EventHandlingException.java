package com.matsg.battlegrounds.game;

public class EventHandlingException extends RuntimeException {

    public EventHandlingException() {
        super();
    }

    public EventHandlingException(String message) {
        super(message);
    }

    public EventHandlingException(String message, Throwable cause) {
        super(message, cause);
    }
}