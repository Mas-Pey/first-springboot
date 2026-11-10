package com.counter.charcounter.exception;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String kalimat) {
        super("Data dengan kalimat " + kalimat + " sudah ada. Ganti yang lain!");
    }
}
