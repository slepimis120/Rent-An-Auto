package com.rentauto.paymentprovider.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentauto.paymentprovider.domain.PaymentStatus;
import com.rentauto.paymentprovider.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByStan(String stan);
    List<Transaction> findByPaymentStatus(PaymentStatus paymentStatus);
    Optional<Transaction> findByExternalTransactionId(String externalTransactionId);
}
