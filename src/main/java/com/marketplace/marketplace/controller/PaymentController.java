package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.order.OrderService;
import com.marketplace.marketplace.payment.PaypalService;
import com.marketplace.marketplace.transaction.Transaction;
import com.marketplace.marketplace.transaction.TransactionService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    private final PaypalService paypalService;
    private final OrderService orderService;
    private final TransactionService transactionService;

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("PayerID") String payerId,
                                 @RequestParam("transactionId") String transactionId,
                                 @RequestParam("orderId") String orderId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                orderService.finalizeOrder(orderId);
                transactionService.transactionSuccess(transactionService.getTransactionByTransactionId(transactionId));
                return "payment successful";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }

        return "payment successful";
    }

    @GetMapping("/cancel")
    public String paymentCancel(@RequestParam("transactionId") String transactionId) {
        transactionService.transactionFail(transactionService.getTransactionByTransactionId(transactionId));
        return "payment canceled";
    }

    @GetMapping("/error")
    public String paymentError() {
        return "payment error";
    }
}
