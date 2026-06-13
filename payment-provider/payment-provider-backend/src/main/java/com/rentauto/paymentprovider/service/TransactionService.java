package com.rentauto.paymentprovider.service;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.TransactionCreateRequest;
import com.rentauto.paymentprovider.api.dto.TransactionCreateResponse;
import com.rentauto.paymentprovider.domain.Merchant;
import com.rentauto.paymentprovider.domain.PaymentMethod;
import com.rentauto.paymentprovider.domain.PaymentStatus;
import com.rentauto.paymentprovider.domain.Transaction;
import com.rentauto.paymentprovider.repository.MerchantRepository;
import com.rentauto.paymentprovider.repository.TransactionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MerchantRepository merchantRepository;
    private final BankClient bankClient;
    private static final Logger log =
        LoggerFactory.getLogger(TransactionService.class);

    public Transaction initPaymentProcess(PaymentInitRequest request, PaymentMethod method) {
        Merchant merchant = merchantRepository.findByMerchantApiKey(request.merchantApiKey())
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));

        log.info(
            "Payment initiated. Merchant={}, Amount={}, Currency={}, Method={}",
            merchant.getId(),
            request.amount(),
            request.currency(),
            method.toString()
        );

        Transaction tx = new Transaction();
        tx.setMerchant(merchant);
        tx.setMerchantOrderId(request.merchantOrderId());
        tx.setMerchantTimestamp(request.merchantTimestamp());
        tx.setAmount(request.amount());
        tx.setCurrency(request.currency());
        tx.setPaymentMethod(method);
        tx.setPaymentStatus(PaymentStatus.PENDING);
        tx.setPspTimestamp(Instant.now());
        String generatedStan = "STAN-" + UUID.randomUUID().toString().substring(0, 8);
        tx.setStan(generatedStan);

        transactionRepository.save(tx);

        TransactionCreateRequest bankRequest = new TransactionCreateRequest(
                request.merchantId(),
                request.amount().toString(),
                request.currency(),
                request.merchantId() + "-" + tx.getStan() + "-" + tx.getPspTimestamp(),
                tx.getPspTimestamp().toString(),
                method.toString()
        );

        try {
            TransactionCreateResponse bankResponse = bankClient.create(bankRequest);

            log.info(
                "Bank transaction created. ExternalTransactionId={}",
                bankResponse.payment_id()
            );

            tx.setExternalTransactionId(bankResponse.payment_id());
            tx.setPaymentUrl(bankResponse.payment_url());

            return transactionRepository.save(tx);
        } catch (Exception e) {
            log.error(
                "Bank communication failed. TransactionId={}",
                tx.getId(),
                e
            );
            
            tx.setPaymentStatus(PaymentStatus.ERROR);
            transactionRepository.save(tx);

            throw new RuntimeException("Bank service communication failed: " + e.getMessage());
        }
    }

    public void update(Transaction tx) {
        transactionRepository.save(tx);
    }

    public Transaction getTransaction(String stan) {
        return transactionRepository.findByStan(stan)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    }

    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    }

    public Transaction getTransactionByExternalId(String externalId) {
        return transactionRepository.findByExternalTransactionId(externalId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    }

}
