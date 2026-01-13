package com.rentanauto.acquirerbank.api.dto;

public record CardPaymentRequest(
        String transactionId,
        String cardHolderName,
        String pan,
        String expiryDate,
        String securityCode
) {}