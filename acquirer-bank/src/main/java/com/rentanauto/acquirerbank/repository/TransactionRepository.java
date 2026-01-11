package com.rentanauto.acquirerbank.repository;

import com.rentanauto.acquirerbank.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Transaction findByStan(String stan);

}
