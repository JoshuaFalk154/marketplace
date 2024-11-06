package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.payment.PaypalService;
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

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "payment successful";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }

        return "payment successful";
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "payment canceled";
    }

    @GetMapping("/error")
    public String paymentError() {
        return "payment error";
    }
}
