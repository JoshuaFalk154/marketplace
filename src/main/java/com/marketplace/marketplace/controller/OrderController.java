package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.DTO.OrderCreate;
import com.marketplace.marketplace.order.Order;
import com.marketplace.marketplace.order.OrderService;
import com.marketplace.marketplace.payment.PaypalService;
import com.marketplace.marketplace.product.ProductService;
import com.marketplace.marketplace.transaction.Transaction;
import com.marketplace.marketplace.transaction.TransactionService;
import com.marketplace.marketplace.transaction.TransactionStatus;
import com.marketplace.marketplace.user.User;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PaypalService paypalService;
    private final OrderService orderService;
    private final TransactionService transactionService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createOrder(@AuthenticationPrincipal User user, @RequestBody OrderCreate orderCreate) {
        Order order = orderService.createOrder(user, orderCreate);

        return new ResponseEntity<>("order with ID: " + order.getOrderId() + " created successful", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Void> placeOrder(
            @RequestParam("method") String method,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description,
            @RequestParam("orderId") String orderId
    ) {
        //TODO
        // if payment successful, decrease stock amount

        Order order = orderService.getOrderByOrderId(orderId);
        Double amount = productService.calculatePriceIfExists(order.getOrderItems());
        String transactionId = UUID.randomUUID().toString().substring(0, 13);


        try {
            String cancelUrl = "http://localhost:8080/payment/cancel?transactionId=" + transactionId;
            String successUrl = "http://localhost:8080/payment/success?transactionId=" + transactionId;
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
                    Transaction transaction = Transaction.builder()
                            .transactionId(transactionId)
                            .status(TransactionStatus.PENDING)
                            .paymentMethod(method)
                            .amount(amount)
                            .order(order)
                            .build();
                    transactionService.save(transaction);
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
