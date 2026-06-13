package com.rentauto.paymentprovider.api.dto;

import java.math.BigDecimal;

public record TransactionResponse(
    String id,
    String stan,
    String merchantId,
    BigDecimal amount,
    String currency,
    String pspTimestamp,
    String acquirerTimestamp,
    String paymentUrl,
    String paymentStatus
) {}