package com.letcafe.exception;

public class BrowserTypeUnsupportedException extends RuntimeException {
    public BrowserTypeUnsupportedException(String message) {
        super(message);
    }
}
