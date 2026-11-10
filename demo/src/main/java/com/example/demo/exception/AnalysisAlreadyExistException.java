package com.example.demo.exception;

public class AnalysisAlreadyExistException extends RuntimeException {
    public AnalysisAlreadyExistException(String kalimat) {
        super("Kalimat '" + kalimat + "' sudah pernah dianalisis");
    }
}
