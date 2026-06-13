package com.rentauto.paymentprovider.plugins.qr;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import com.rentauto.paymentprovider.plugins.PaymentPlugin;
import com.rentauto.paymentprovider.service.TransactionService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class QrCodePaymentPlugin implements PaymentPlugin {

    private final TransactionService transactionService;
    private final QrGenerator qrGenerator;

    @Override
    public PaymentInitResponse processPayment(PaymentInitRequest request) {
        return null;
    }

    @Override
    public String handleCallback(Map<String, String> params) {
        return "";
    }
}
