package com.povilas.eagle_bank.user.web;

import com.povilas.eagle_bank.user.domain.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ErrorDetail(String field, String message, String type) {}

    record BadRequestErrorResponse(String message, List<ErrorDetail> details) {}

    record ErrorResponse(String message) {}

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ErrorDetail(e.getField(), e.getDefaultMessage(), "VALIDATION_ERROR"))
                .toList();
        return new BadRequestErrorResponse("Validation failed", details);
    }
}
