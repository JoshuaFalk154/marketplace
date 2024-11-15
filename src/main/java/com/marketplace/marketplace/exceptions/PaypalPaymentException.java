package com.marketplace.marketplace.exceptions;

public class PaypalPaymentException extends RuntimeException{
    public PaypalPaymentException(String message) {
        super(message);
    }
}
