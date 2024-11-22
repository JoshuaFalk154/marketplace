package com.marketplace.marketplace.DTO;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Double amount;
    private String currency;
    private String description;
    private String orderId;
    private String method;
    private String cancelUrl;
    private String successUrl;
    private Map<String, String> additionalParams;
}
