package com.matsg.battlegrounds.storage;

public class LocationFormatException extends RuntimeException {

    public LocationFormatException() {
        super();
    }

    public LocationFormatException(String message) {
        super(message);
    }

    public LocationFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
