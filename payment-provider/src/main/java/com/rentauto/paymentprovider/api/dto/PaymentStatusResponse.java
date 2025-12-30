package com.rentauto.paymentprovider.api.dto;

import com.rentauto.paymentprovider.domain.PaymentStatus;

public record PaymentStatusResponse(
        String stan,
        PaymentStatus status
) {
}
