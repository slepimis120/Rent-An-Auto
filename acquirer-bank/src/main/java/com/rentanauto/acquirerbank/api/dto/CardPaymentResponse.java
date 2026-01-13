package com.rentanauto.acquirerbank.api.dto;

public record CardPaymentResponse(
        String status,
        String globalTransactionId,
        String acquirerTimestamp,
        String stan,
        String redirectUrl
) {}