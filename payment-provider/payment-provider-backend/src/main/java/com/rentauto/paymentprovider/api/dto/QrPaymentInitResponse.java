package com.rentauto.paymentprovider.api.dto;

public record QrPaymentInitResponse(
        String stan,
        String paymentUrl,
        String qrCode,
        String status
) {
}
