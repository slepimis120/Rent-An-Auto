package com.rentauto.paymentprovider.api.dto;

public record RegisterRequest(
        String email,
        String password
) {
}
