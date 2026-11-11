package com.example.quote_service.exception;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FeignErrorHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException feignException) {
        int status = feignException.status() > 0 ? feignException.status() : 503;
        return ResponseEntity.status(status).body(feignException.getMessage());
    }
}
