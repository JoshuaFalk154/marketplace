package com.marketplace.marketplace.exceptions;

public class ResourceNotOwnerException extends RuntimeException{
    public ResourceNotOwnerException(String message) {
        super(message);
    }
}
