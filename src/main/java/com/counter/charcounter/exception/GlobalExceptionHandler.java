package com.counter.charcounter.exception;

import com.counter.charcounter.dto.response.BaseResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<BaseResponse<Object>> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(BaseResponse.error(message));
    }

    @ExceptionHandler(QuoteUnavailableException.class)
    public ResponseEntity<BaseResponse<Object>> handleQuoteUnavailableException(QuoteUnavailableException quoteUnavailableException) {
        log.info("quoteUnavailableException: " + quoteUnavailableException);
        return build(HttpStatus.SERVICE_UNAVAILABLE, quoteUnavailableException.getMessage());
    }

    @ExceptionHandler(AnalysisNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleNotFound(AnalysisNotFoundException exception){
        return build(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<BaseResponse<Object>> handleAlreadyExist(AlreadyExistException exception) {
        return build(HttpStatus.CONFLICT, exception.getMessage());
    }

    // Thrown when @Valid @RequestBody fails
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidation(MethodArgumentNotValidException validException) {
        List<String> message = new ArrayList<>();
        for (FieldError fieldError : validException.getBindingResult().getFieldErrors()) {
            message.add(fieldError.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, String.join(",", message));
    }

    // Thrown when @Validated @PathVariable or @RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<Object>> handleConstraintViolation(ConstraintViolationException constraintViolationException) {
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation<?> violation : constraintViolationException.getConstraintViolations()) {
            messages.add(violation.getMessage());
        }
        return build(HttpStatus.BAD_REQUEST, String.join(", ", messages));
    }

}
