package com.julian.razif.kanban.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(
    BadRequestException ex) {

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(
    NotFoundException ex) {

    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentials(
    BadCredentialsException ex) {

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(
    MethodArgumentNotValidException ex) {

    String message = ex
      .getBindingResult()
      .getAllErrors()
      .stream()
      .map(error -> {
        if (error instanceof FieldError fieldError) {
          return fieldError.getField() + " " + fieldError.getDefaultMessage();
        }

        return error.getDefaultMessage();
      })
      .collect(Collectors.joining(", "));

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(new ErrorResponse(message));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(
    AccessDeniedException ex) {

    return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .body(new ErrorResponse("Access denied"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(
    Exception ex) {

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ErrorResponse("Internal server error"));
  }

  public record ErrorResponse(String message) {
  }

}
