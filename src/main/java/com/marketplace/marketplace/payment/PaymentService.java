package com.marketplace.marketplace.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    public String successUrl(String orderId) {
        return "http://localhost:8080/payment/success?transactionId=" + "&orderId=" + orderId;
    }

    public String cancelUrl(String orderId) {
        return "http://localhost:8080/payment/cancel?transactionId=" + "&orderId=" + orderId;
    }
}
