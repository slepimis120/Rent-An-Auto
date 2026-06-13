package com.rentauto.paymentprovider.api.dto;

public record TransactionCreateRequest (
        String merchantId,
        String amount,
        String currency,
        String stan,
        String psp_timestamp,
        String paymentMethod
){
}
