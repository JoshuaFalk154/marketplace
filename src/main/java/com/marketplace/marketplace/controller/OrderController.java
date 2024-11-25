package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.DTO.OrderCreate;
import com.marketplace.marketplace.DTO.PaymentRequest;
import com.marketplace.marketplace.DTO.PaymentResponse;
import com.marketplace.marketplace.exceptions.PaymentException;
import com.marketplace.marketplace.order.Order;
import com.marketplace.marketplace.order.OrderService;
import com.marketplace.marketplace.orderItem.OrderItemService;
import com.marketplace.marketplace.payment.PaymentService;
import com.marketplace.marketplace.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity<String> createOrder(@AuthenticationPrincipal User user, @RequestBody OrderCreate orderCreate) {
        Order order = orderService.createOrder(user, orderCreate);

        return new ResponseEntity<>("order with ID: " + order.getOrderId() + " created successful", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Void> placeOrder(@RequestParam("method") String method, @RequestParam("currency") String currency,
                                           @RequestParam("description") String description, @RequestParam("orderId") String orderId
    ) {
        try {
            Order order = orderService.getOrderByOrderId(orderId);
            Double amount = orderItemService.calculatePrice(order.getOrderItems());
            String successUrl = paymentService.getSuccessUrl(orderId);
            String cancelUrl = paymentService.getCancelUrl(orderId);
            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .amount(amount)
                    .currency(currency)
                    .description(description)
                    .orderId(orderId)
                    .method(method)
                    .cancelUrl(cancelUrl)
                    .successUrl(successUrl)
                    .build();

            PaymentResponse response = paymentService.processPayment(paymentRequest);

            if (response.isCompleted()) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else if (response.getApprovalUrl().isPresent()) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(response.getApprovalUrl().get())
                        .build();
            }
        } catch (PaymentException e) {
            log.error("Payment processing error: ", e);
        }
        URI errorUri = URI.create("http://localhost:8080/payment/error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .location(errorUri)
                .build();
    }
}
