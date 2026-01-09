package com.rentauto.paymentprovider.api.dto;

import com.rentauto.paymentprovider.domain.PaymentMethod;

import java.math.BigDecimal;

public record PaymentInitRequest(
        String merchantCode,
        String merchantOrderId,
        BigDecimal amount,
        String currency,
        PaymentMethod paymentMethod
) {
}
