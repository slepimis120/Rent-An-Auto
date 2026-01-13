package com.rentauto.paymentprovider.api.dto;

public record BankNotificationRequest(
        String stan,
        String status,
        String globalTransactionId,
        String acquirerTimestamp
) {}