package com.marketplace.marketplace.payment;

import com.marketplace.marketplace.order.Order;
import com.marketplace.marketplace.order.OrderService;
import com.marketplace.marketplace.product.ProductService;
import com.marketplace.marketplace.transaction.TransactionService;
import com.marketplace.marketplace.transaction.TransactionStatus;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext;

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

    public Optional<URI> processPayment(String method, String currency, String description, Double amount, String orderId, String cancelUrl, String successUrl) throws PayPalRESTException {
        Payment payment = createPayment(
                amount,
                currency,
                method,
                "sale",
                description,
                cancelUrl,
                successUrl
        );

        // TODO
        // EVENT payment created successful

        return extractApprovalUrl(payment);
    }

    private Optional<URI> extractApprovalUrl(Payment payment) {
        return payment.getLinks().stream()
                .filter(link -> link.getRel().equals("approval_url"))
                .map(link -> URI.create(link.getHref()))
                .findFirst();
    }
}
