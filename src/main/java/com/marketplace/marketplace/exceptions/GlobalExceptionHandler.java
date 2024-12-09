package com.marketplace.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidArgumentsException.class)
    public ResponseEntity<String> handleInvalidArgumentsException(InvalidArgumentsException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<String> handlePaymentException(PaymentException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<String> handleProductAlreadyExistsException(ProductAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotOwnerException.class)
    public ResponseEntity<String> handleResourceNotOwnerException(ResourceNotOwnerException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(UserInvalidArgumentsException.class)
    public ResponseEntity<String> handleUserInvalidArgumentsException(UserInvalidArgumentsException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }











}
