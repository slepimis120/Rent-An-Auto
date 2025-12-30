package com.rentauto.paymentprovider.service;

import com.rentauto.paymentprovider.domain.Merchant;
import com.rentauto.paymentprovider.domain.PaymentStatus;
import com.rentauto.paymentprovider.domain.Transaction;
import com.rentauto.paymentprovider.repository.MerchantRepository;
import com.rentauto.paymentprovider.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MerchantRepository merchantRepository;

    public TransactionService(TransactionRepository transactionRepository, MerchantRepository merchantRepository) {
        this.transactionRepository = transactionRepository;
        this.merchantRepository = merchantRepository;
    }

    public Transaction createTransaction(Transaction tx, String merchantCode) {
        Merchant merchant = merchantRepository.findByMerchantCode(merchantCode)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));

        tx.setMerchant(merchant);
        tx.setPaymentStatus(PaymentStatus.CREATED);
        tx.setPspTimestamp(Instant.now());
        tx.setStan(UUID.randomUUID().toString());

        return transactionRepository.save(tx);
    }

    public Transaction getTransaction(String stan) {
        return transactionRepository.findByStan(stan)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    }
}
