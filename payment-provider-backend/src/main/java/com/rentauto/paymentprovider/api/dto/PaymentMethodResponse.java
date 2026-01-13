package com.rentauto.paymentprovider.api.dto;

import com.rentauto.paymentprovider.domain.PaymentMethod;

public record PaymentMethodResponse(
        String stan,
        PaymentMethod method
) {
}
