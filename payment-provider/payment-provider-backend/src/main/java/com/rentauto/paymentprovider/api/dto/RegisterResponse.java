package com.rentauto.paymentprovider.api.dto;

public record RegisterResponse (
    String merchantId,
    String merchantApiKey
) {
}
