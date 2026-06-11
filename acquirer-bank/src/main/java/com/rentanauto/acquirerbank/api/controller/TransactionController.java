package com.rentanauto.acquirerbank.api.controller;

import com.rentanauto.acquirerbank.api.dto.CardPaymentRequest;
import com.rentanauto.acquirerbank.api.dto.CardPaymentResponse;
import com.rentanauto.acquirerbank.api.dto.TransactionCreateRequest;
import com.rentanauto.acquirerbank.api.dto.TransactionCreateResponse;
import com.rentanauto.acquirerbank.domain.Transaction;
import com.rentanauto.acquirerbank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:4300")
public class TransactionController {
    private final TransactionService service;

    @PostMapping()
    public ResponseEntity<TransactionCreateResponse> createTransaction(@RequestBody TransactionCreateRequest request) {
        TransactionCreateResponse transaction = service.createTransaction(request);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        Transaction transaction = service.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/pay")
    public ResponseEntity<CardPaymentResponse> processPayment(@RequestBody CardPaymentRequest request) {
        return ResponseEntity.ok(service.processPayment(request));
    }
}
