package com.rentauto.paymentprovider.api.dto;

public record LoginRequest(
        String email,
        String password
) {
}
