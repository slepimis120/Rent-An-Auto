package com.rentauto.paymentprovider.api.controller;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import com.rentauto.paymentprovider.api.dto.PaymentStatusResponse;
import com.rentauto.paymentprovider.api.mapper.TransactionMapper;
import com.rentauto.paymentprovider.domain.Transaction;
import com.rentauto.paymentprovider.service.TransactionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentInitController {

    private final TransactionService service;
    private final TransactionMapper mapper;

    public PaymentInitController(TransactionService service, TransactionMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/init")
    public PaymentInitResponse initPayment(@RequestBody PaymentInitRequest request) {
        Transaction tx = mapper.toEntity(request);
        tx = service.createTransaction(tx, request.merchantCode());
        String paymentUrl = "http://localhost:4200/pay/" + tx.getStan();
        return mapper.toInitResponse(tx, paymentUrl);
    }

    @GetMapping("/status/{stan}")
    public PaymentStatusResponse getPaymentStatus(@PathVariable String stan) {
        Transaction tx = service.getTransaction(stan);
        return mapper.toStatusResponse(tx);
    }
}
