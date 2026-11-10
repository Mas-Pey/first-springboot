package com.example.demo.exception;

import com.example.demo.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AnalysisNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleNotFound(AnalysisNotFoundException exception) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AnalysisAlreadyExistException.class)
    public ResponseEntity<BaseResponse<Object>> handleAlreadyExist(
            AnalysisAlreadyExistException exception) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage());
            }

    private ResponseEntity<BaseResponse<Object>> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(BaseResponse.error(message));
    }

}
