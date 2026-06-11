package com.rentauto.paymentprovider.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentauto.paymentprovider.api.dto.PaymentMethodResponse;
import com.rentauto.paymentprovider.api.dto.PaymentStatusResponse;
import com.rentauto.paymentprovider.api.mapper.TransactionMapper;
import com.rentauto.paymentprovider.domain.Transaction;
import com.rentauto.paymentprovider.service.TransactionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentInitController {

    private final TransactionService service;
    private final TransactionMapper mapper;

    @GetMapping("/status/{stan}")
    public PaymentStatusResponse getPaymentStatus(@PathVariable String stan) {
        Transaction tx = service.getTransaction(stan);
        return mapper.toStatusResponse(tx);
    }

    @GetMapping("/type/{stan}")
    public PaymentMethodResponse getPaymentType(@PathVariable String stan) {
        Transaction tx = service.getTransaction(stan);
        return mapper.toMethodResponse(tx);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getPaymentDetails(@PathVariable String id) {
        Transaction tx = service.getTransactionById(id);
        return ResponseEntity.ok(tx);
    }

    @GetMapping("/external/{id}")
    public ResponseEntity<Transaction> getPaymentDetailsByExternalId(@PathVariable String id) {
        Transaction tx = service.getTransactionByExternalId(id);
        return ResponseEntity.ok(tx);
    }
}
