package com.rentauto.paymentprovider.api.controller;

import com.rentauto.paymentprovider.api.dto.*;
import com.rentauto.paymentprovider.api.mapper.TransactionMapper;
import com.rentauto.paymentprovider.domain.Transaction;
import com.rentauto.paymentprovider.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
