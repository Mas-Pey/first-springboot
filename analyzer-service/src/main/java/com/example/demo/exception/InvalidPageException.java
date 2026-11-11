package com.example.demo.exception;

public class InvalidPageException extends RuntimeException {
    public InvalidPageException(String message) {
        super(message);
    }
}
