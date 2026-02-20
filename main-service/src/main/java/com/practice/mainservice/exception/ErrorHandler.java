package com.practice.mainservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotFoundException(CategoryNotFoundException ex) {
        List<String> stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .toList();

        return ApiError.builder()
                .errors(stackTrace)
                .message(ex.getMessage())
                .reason("Category not found")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(UserNotFoundException ex) {
        List<String> stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .toList();

        return ApiError.builder()
                .errors(stackTrace)
                .message(ex.getMessage())
                .reason("User not found")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventNotFoundException(EventNotFoundException ex) {
        List<String> stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .toList();

        return ApiError.builder()
                .errors(stackTrace)
                .message(ex.getMessage())
                .reason("Event not found")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationNotFoundException(CompilationNotFoundException ex) {
        List<String> stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .toList();

        return ApiError.builder()
                .errors(stackTrace)
                .message(ex.getMessage())
                .reason("Compilation not found")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException ex) {
        List<String> stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .toList();

        return ApiError.builder()
                .errors(stackTrace)
                .message(ex.getMessage())
                .reason("Conflict exception")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        List<String> stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .toList();

        return ApiError.builder()
                .errors(stackTrace)
                .message(ex.getMessage())
                .reason("Data Integrity Violation Exception")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException ex) {
        List<String> stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .toList();

        return ApiError.builder()
                .errors(stackTrace)
                .message(ex.getMessage())
                .reason("Validation Exception")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
