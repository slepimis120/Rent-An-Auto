package com.rentanauto.acquirerbank.service;

import com.rentanauto.acquirerbank.api.dto.TransactionCreateRequest;
import com.rentanauto.acquirerbank.api.dto.TransactionCreateResponse;
import com.rentanauto.acquirerbank.domain.PaymentStatus;
import com.rentanauto.acquirerbank.domain.Transaction;
import com.rentanauto.acquirerbank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionCreateResponse createTransaction(TransactionCreateRequest request) {
        Transaction existing = transactionRepository.findByStan(request.stan());
        if (existing != null) {
            return new TransactionCreateResponse(existing.getPaymentUrl(), existing.getId().toString());
        }

        Transaction transaction = new Transaction();
        transaction.setMerchantId(request.merchantId());
        transaction.setAmount(new BigDecimal(request.amount()));
        transaction.setCurrency(request.currency());
        transaction.setStan(request.stan());
        transaction.setPspTimestamp(request.psp_timestamp());
        transaction.setAcquirerTimestamp(Instant.now().toString());
        transaction.setPaymentStatus(PaymentStatus.CREATED);

        String paymentUrl = "https://localhost:4200/pay/" + request.stan();
        transaction.setPaymentUrl(paymentUrl);

        transactionRepository.save(transaction);

        return new TransactionCreateResponse(paymentUrl, transaction.getId().toString());
    }

}
