package com.rentauto.paymentprovider.api.dto;

public record TransactionCreateResponse(
        String payment_url,
        String payment_id
) {
}
