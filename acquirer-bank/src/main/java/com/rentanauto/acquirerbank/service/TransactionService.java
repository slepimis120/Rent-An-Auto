package com.rentanauto.acquirerbank.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rentanauto.acquirerbank.api.dto.CardPaymentRequest;
import com.rentanauto.acquirerbank.api.dto.CardPaymentResponse;
import com.rentanauto.acquirerbank.api.dto.TransactionCreateRequest;
import com.rentanauto.acquirerbank.api.dto.TransactionCreateResponse;
import com.rentanauto.acquirerbank.domain.CardHolder;
import com.rentanauto.acquirerbank.domain.PaymentStatus;
import com.rentanauto.acquirerbank.domain.Transaction;
import com.rentanauto.acquirerbank.repository.CardHolderRepository;
import com.rentanauto.acquirerbank.repository.TransactionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardHolderRepository cardHolderRepository;
    private final RestTemplate restTemplate = new RestTemplate();

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

        transaction = transactionRepository.save(transaction);

        String paymentUrl = "http://localhost:4300/pay/" + transaction.getId();
        transaction.setPaymentUrl(paymentUrl);

        transactionRepository.save(transaction);

        return new TransactionCreateResponse(paymentUrl, transaction.getId().toString());
    }

    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    }

    public CardPaymentResponse processPayment(CardPaymentRequest request) {

        Transaction transaction = transactionRepository.findById(
                UUID.fromString(request.transactionId()))
                .orElseThrow(() ->
                        new IllegalArgumentException("Transaction not found"));

        CardHolder cardHolder = cardHolderRepository.findByPan(request.pan().replaceAll("\\s+", ""))
                .orElseThrow(() ->
                        new IllegalArgumentException("Card not found: " + request));
                        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

        YearMonth cardExpiry = YearMonth.parse(request.expiryDate(), formatter);
        YearMonth now = YearMonth.now();

        boolean notExpired = !cardExpiry.isBefore(now);

        boolean validCard =
                cardHolder.isActive()
                && cardHolder.getSecurityCode().equals(request.securityCode())
                && cardHolder.getExpiryDate().equals(request.expiryDate())
                && cardHolder.getFullName().equalsIgnoreCase(request.cardHolderName())
                && notExpired;

        boolean hasFunds =
                cardHolder.getBalance()
                        .compareTo(transaction.getAmount()) >= 0;

        String status;

        if (validCard && hasFunds) {
            cardHolder.setBalance(
                    cardHolder.getBalance()
                            .subtract(transaction.getAmount()));

            cardHolderRepository.save(cardHolder);

            transaction.setPaymentStatus(PaymentStatus.SUCCESS);

            status = "SUCCESS";

        } else {
            transaction.setPaymentStatus(PaymentStatus.FAILED);

            status = "FAILED";
        }

        transactionRepository.save(transaction);

        Map<String, String> pspPayload = new HashMap<>();
        pspPayload.put("stan", transaction.getStan());
        pspPayload.put("status", status);
        pspPayload.put("globalTransactionId", transaction.getId().toString());
        pspPayload.put("acquirerTimestamp", transaction.getAcquirerTimestamp());

        String finalRedirectUrlFromPsp = sendNotification(pspPayload);

        return new CardPaymentResponse(
                status,
                transaction.getId().toString(),
                transaction.getAcquirerTimestamp(),
                transaction.getStan(),
                finalRedirectUrlFromPsp
        );
    }

    public String sendNotification(Map<String, String> payload) {
        String pspUrl = "http://payment-provider-backend:8080/payments/card/bank-card";
        return restTemplate.postForObject(pspUrl, payload, String.class);
    }
}
