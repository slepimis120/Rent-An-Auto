package com.rentauto.paymentprovider.api.dto;

import java.math.BigDecimal;

public record PaymentInitRequest(
        String merchantId,
        String merchantApiKey,
        String merchantOrderId,
        String merchantTimestamp,
        BigDecimal amount,
        String currency
) {
}
