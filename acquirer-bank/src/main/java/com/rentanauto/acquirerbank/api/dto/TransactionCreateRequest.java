package com.rentanauto.acquirerbank.api.dto;

public record TransactionCreateRequest (
    String merchantId,
    String amount,
    String currency,
    String stan,
    String psp_timestamp
){
}
