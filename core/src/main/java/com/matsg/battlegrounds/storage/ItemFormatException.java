package com.matsg.battlegrounds.storage;

public class ItemFormatException extends Exception {

    public ItemFormatException() {
        super();
    }

    public ItemFormatException(String message) {
        super(message);
    }

    public ItemFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
