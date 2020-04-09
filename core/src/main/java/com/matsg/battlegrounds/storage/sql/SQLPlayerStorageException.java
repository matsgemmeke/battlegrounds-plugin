package com.matsg.battlegrounds.storage.sql;

public class SQLPlayerStorageException extends RuntimeException {

    public SQLPlayerStorageException() {
        super();
    }

    public SQLPlayerStorageException(String message) {
        super(message);
    }

    public SQLPlayerStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
