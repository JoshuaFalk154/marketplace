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
    private final TransactionService transactionService;

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("PayerID") String payerId,
                                 @RequestParam("orderId") String orderId) {
        //Transaction transaction = transactionService.getTransactionByTransactionId(paymentId);
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                // TODO
                // EVENT: payment successful
                // set transaction on success
                // finalize order
                return "payment successful";
            }
        } catch (PayPalRESTException e) {
            // TODO
            // EVENT: payment failed
            log.error("Error occurred:: ", e);
        }

        return "payment successful";
    }

    @GetMapping("/cancel")
    public String paymentCancel(@RequestParam("transactionId") String transactionId) {
        // EVENT: payment failed
        // transaction failed
        //transactionService.transactionFail(transactionService.getTransactionByTransactionId(transactionId));
        return "payment canceled";
    }

    @GetMapping("/error")
    public String paymentError() {
        return "payment error";
    }
}
