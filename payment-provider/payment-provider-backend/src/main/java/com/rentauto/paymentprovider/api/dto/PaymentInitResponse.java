package com.rentauto.paymentprovider.api.dto;

public record PaymentInitResponse(
        String stan,
        String paymentUrl,
        String status
) {
}
