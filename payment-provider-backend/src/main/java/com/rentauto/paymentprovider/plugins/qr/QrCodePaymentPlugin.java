package com.rentauto.paymentprovider.plugins.qr;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import com.rentauto.paymentprovider.domain.PaymentMethod;
import com.rentauto.paymentprovider.domain.PaymentStatus;
import com.rentauto.paymentprovider.domain.Transaction;
import com.rentauto.paymentprovider.plugins.PaymentPlugin;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class QrCodePaymentPlugin implements PaymentPlugin {

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
