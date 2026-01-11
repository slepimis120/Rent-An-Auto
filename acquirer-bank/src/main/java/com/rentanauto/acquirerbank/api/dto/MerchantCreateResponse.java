package com.rentanauto.acquirerbank.api.dto;

import java.util.UUID;

public record MerchantCreateResponse(
        UUID merchantId,
        String name,
        UUID acquirerContractId,
        boolean isActive
) {}