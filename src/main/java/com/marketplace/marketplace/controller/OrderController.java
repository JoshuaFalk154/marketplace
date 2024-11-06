package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.payment.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PaypalService paypalService;

    @GetMapping
    public ResponseEntity<Void> placeOrder(
            @RequestParam("method") String method,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description
    ) {
        Double amount = 24.44;
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";
            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    URI approvalUri = URI.create(link.getHref());
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .location(approvalUri)
                            .build();
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred: ", e);
        }

        URI errorUri = URI.create("http://localhost:8080/payment/error");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .location(errorUri)
                .build();
    }

}
