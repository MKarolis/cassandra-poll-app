package com.karolismed.cassandra.polling.app.core.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
