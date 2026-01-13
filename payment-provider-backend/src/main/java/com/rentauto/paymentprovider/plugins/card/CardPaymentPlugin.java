package com.rentauto.paymentprovider.plugins.card;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import com.rentauto.paymentprovider.api.mapper.TransactionMapper;
import com.rentauto.paymentprovider.domain.PaymentMethod;
import com.rentauto.paymentprovider.domain.Transaction;
import com.rentauto.paymentprovider.plugins.PaymentPlugin;
import com.rentauto.paymentprovider.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardPaymentPlugin implements PaymentPlugin {

    private final TransactionService transactionService;
    private final TransactionMapper mapper;

    @Override
    public PaymentInitResponse processPayment(PaymentInitRequest request) {
        Transaction tx = transactionService.initPaymentProcess(request, PaymentMethod.CARD);
        return mapper.toInitResponse(tx, tx.getPaymentUrl());
    }
}

