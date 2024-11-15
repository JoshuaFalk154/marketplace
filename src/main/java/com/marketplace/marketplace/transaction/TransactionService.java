package com.marketplace.marketplace.transaction;

import com.marketplace.marketplace.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) {
        // TODO
        // check validity of transaction object
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionByTransactionId(String transactionId) {
        return transactionRepository.findTransactionByTransactionId(transactionId)
                .orElseThrow((() -> new ResourceNotFoundException("transaction with id: " + transactionId + " not found")));
    }

    public void transactionFail(Transaction transaction) {
        transaction.setStatus(TransactionStatus.FAILED);
        save(transaction);
    }

    public void transactionSuccess(Transaction transaction) {
        transaction.setStatus(TransactionStatus.SUCCESS);
        save(transaction);
    }

//    public String generateTransactionId() {
//        return UUID.randomUUID().toString().substring(0, 13);
//    }
}
