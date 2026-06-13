package com.rentauto.paymentprovider.plugins;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;

import java.util.Map;

public interface PaymentPlugin {
    PaymentInitResponse processPayment(PaymentInitRequest request);
    String handleCallback(Map<String, String> params);
}
