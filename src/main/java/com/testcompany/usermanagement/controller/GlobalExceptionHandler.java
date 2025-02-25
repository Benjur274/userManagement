package com.testcompany.usermanagement.controller;

import com.testcompany.usermanagement.exceptions.UserAlreadyExistsException;
import com.testcompany.usermanagement.exceptions.UserNotFoundException;
import com.testcompany.usermanagement.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND, e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.FORBIDDEN, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, "Validation failed", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
}
