package com.rentauto.paymentprovider.plugins.card;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import com.rentauto.paymentprovider.api.mapper.TransactionMapper;
import com.rentauto.paymentprovider.domain.PaymentMethod;
import com.rentauto.paymentprovider.domain.PaymentStatus;
import com.rentauto.paymentprovider.domain.Transaction;
import com.rentauto.paymentprovider.plugins.PaymentPlugin;
import com.rentauto.paymentprovider.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    @Override
    public String handleCallback(Map<String, String> params) {
        String fullStan = params.get("stan");
        String status = params.get("status");
        String globalId = params.get("globalTransactionId");

        String[] parts = fullStan.split("-");
        String originalStan = parts[6];

        Transaction tx = transactionService.getTransaction(originalStan);

        if ("SUCCESS".equalsIgnoreCase(status)) {
            tx.setPaymentStatus(PaymentStatus.SUCCESS);
        } else {
            tx.setPaymentStatus(PaymentStatus.FAILED);
        }

        tx.setExternalTransactionId(globalId);
        transactionService.update(tx);

        String redirectUrl = tx.getPaymentStatus() == PaymentStatus.SUCCESS
                ? tx.getMerchant().getSuccessUrl()
                : tx.getMerchant().getFailedUrl();

        return redirectUrl + "?id=" + globalId;
    }

}

