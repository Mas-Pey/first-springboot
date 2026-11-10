package com.counter.charcounter.exception;

public class QuoteUnavailableException extends RuntimeException {
    public QuoteUnavailableException() {
        super("Quote service unavailable");
    }
}
