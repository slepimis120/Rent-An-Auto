package com.rentauto.paymentprovider.repository;

import com.rentauto.paymentprovider.domain.PaymentStatus;
import com.rentauto.paymentprovider.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByStan(String stan);
    List<Transaction> findByMerchantCode(String merchantCode);
    List<Transaction> findByPaymentStatus(PaymentStatus paymentStatus);
}
