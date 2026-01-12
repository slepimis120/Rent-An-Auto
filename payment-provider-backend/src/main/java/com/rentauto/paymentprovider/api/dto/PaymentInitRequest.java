package com.rentauto.paymentprovider.api.dto;

import com.rentauto.paymentprovider.domain.PaymentMethod;

import java.math.BigDecimal;

public record PaymentInitRequest(
        String merchantId,
        String merchantApiKey,
        String merchantOrderId,
        String merchantTimestamp,
        BigDecimal amount,
        String currency,
        PaymentMethod paymentMethod
) {
}
