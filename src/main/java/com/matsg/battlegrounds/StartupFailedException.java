package com.matsg.battlegrounds;

public class StartupFailedException extends Exception {

    public StartupFailedException() {
        super();
    }

    public StartupFailedException(String message) {
        super(message);
    }

    public StartupFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}