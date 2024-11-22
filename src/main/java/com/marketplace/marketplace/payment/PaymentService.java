package com.marketplace.marketplace.payment;

import com.marketplace.marketplace.DTO.PaymentRequest;
import com.marketplace.marketplace.DTO.PaymentResponse;
import com.marketplace.marketplace.exceptions.PaymentException;
import org.springframework.stereotype.Service;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request) throws PaymentException;

    String getSuccessUrl(String orderId);

    String getCancelUrl(String orderId);


}
