package com.example.demo.exception;

public class UnauthrorizedException extends RuntimeException {
    public UnauthrorizedException(String message) {
        super(message);
    }
}
