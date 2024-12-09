package com.marketplace.marketplace.exceptions;

public class ProductAlreadyExistsException extends RuntimeException{
    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
