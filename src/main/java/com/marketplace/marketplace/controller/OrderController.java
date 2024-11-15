package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.DTO.OrderCreate;
import com.marketplace.marketplace.exceptions.PaypalPaymentException;
import com.marketplace.marketplace.order.Order;
import com.marketplace.marketplace.order.OrderService;
import com.marketplace.marketplace.payment.PaymentService;
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
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PaypalService paypalService;
    private final OrderService orderService;
    private final ProductService productService;
    private final PaymentService paymentService;

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
            Double amount = productService.calculatePriceIfExists(order.getOrderItems());
            String successUrl = paymentService.successUrl(orderId);
            String cancelUrl = paymentService.cancelUrl(orderId);

            URI approvalUri = paypalService.processPayment(method, currency, description, amount, orderId, cancelUrl, successUrl)
                    .orElseThrow(() -> new PaypalPaymentException("No approval URL found"));

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(approvalUri)
                    .build();

        } catch (PayPalRESTException | PaypalPaymentException e) {
            log.error("Payment processing error: ", e);
            log.error("Payment processing error: ", e);
            URI errorUri = URI.create("http://localhost:8080/payment/error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .location(errorUri)
                    .build();
        }
    }


//    @GetMapping
//    public ResponseEntity<Void> placeOrder(
//            @RequestParam("method") String method,
//            @RequestParam("currency") String currency,
//            @RequestParam("description") String description,
//            @RequestParam("orderId") String orderId
//    ) {
//        try {
//            Order order = orderService.getOrderByOrderId(orderId);
//            Double amount = productService.calculatePriceIfExists(order.getOrderItems());
//            String transactionId = transactionService.generateTransactionId();
//            String cancelUrl = "http://localhost:8080/payment/cancel?transactionId=" + transactionId + "&orderId=" + orderId;
//            String successUrl = "http://localhost:8080/payment/success?transactionId=" + transactionId + "&orderId=" + orderId;
//
//            Payment payment = paypalService.createPayment(
//                    amount,
//                    currency,
//                    method,
//                    "sale",
//                    description,
//                    cancelUrl,
//                    successUrl
//            );
//
//            for (Links link : payment.getLinks()) {
//                if (link.getRel().equals("approval_url")) {
//                    URI approvalUri = URI.create(link.getHref());
//                    Transaction transaction = Transaction.builder()
//                            .transactionId(transactionId)
//                            .status(TransactionStatus.PENDING)
//                            .paymentMethod(method)
//                            .amount(amount)
//                            .order(order)
//                            .build();
//                    transactionService.save(transaction);
//                    return ResponseEntity.status(HttpStatus.FOUND)
//                            .location(approvalUri)
//                            .build();
//                }
//            }
//        } catch (PayPalRESTException e) {
//            log.error("Error occurred: ", e);
//        }
//        URI errorUri = URI.create("http://localhost:8080/payment/error");
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .location(errorUri)
//                .build();
//
//    }

}
