package com.example.demo.exception;

public class InvalidJumlahException extends RuntimeException {
    public InvalidJumlahException() {
        super("Jumlah quote harus 1 sampai 10");
    }
}
