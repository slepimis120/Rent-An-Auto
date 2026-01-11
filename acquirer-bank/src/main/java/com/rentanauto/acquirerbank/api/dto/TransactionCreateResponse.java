package com.rentanauto.acquirerbank.api.dto;

public record TransactionCreateResponse(
        String payment_url,
        String payment_id
) {
}
