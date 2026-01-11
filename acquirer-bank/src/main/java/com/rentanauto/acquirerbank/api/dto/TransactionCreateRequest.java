package com.rentanauto.acquirerbank.api.dto;

public record TransactionCreateRequest (
    String merchant_id,
    String amount,
    String currency,
    String stan,
    String psp_timestamp
){
}
