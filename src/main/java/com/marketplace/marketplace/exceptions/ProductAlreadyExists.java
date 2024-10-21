package com.marketplace.marketplace.exceptions;

public class ProductAlreadyExists extends RuntimeException{
    public ProductAlreadyExists(String message) {
        super(message);
    }
}
