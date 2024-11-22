package com.marketplace.marketplace.DTO;

import lombok.*;

import java.net.URI;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Optional<URI> approvalUrl;
    private boolean isCompleted;
    private Optional<String> transactionId;
}
