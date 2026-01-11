package com.rentanauto.acquirerbank.service;

import com.rentanauto.acquirerbank.api.dto.MerchantCreateRequest;
import com.rentanauto.acquirerbank.api.dto.MerchantCreateResponse;
import com.rentanauto.acquirerbank.domain.Merchant;
import com.rentanauto.acquirerbank.repository.MerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MerchantService {
    private final MerchantRepository repository;

    public MerchantCreateResponse create(MerchantCreateRequest request) {
        Merchant merchant = new Merchant();
        merchant.setName(request.name());
        merchant.setActive(true);
        merchant.setAcquirerContractId(UUID.randomUUID());

        Merchant saved = repository.save(merchant);

        return new MerchantCreateResponse(
                saved.getMerchantId(),
                saved.getName(),
                saved.getAcquirerContractId(),
                saved.isActive()
        );
    }

    public MerchantCreateResponse getById(String id) {
        Merchant merchant = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));

        return new MerchantCreateResponse(
                merchant.getMerchantId(),
                merchant.getName(),
                merchant.getAcquirerContractId(),
                merchant.isActive()
        );
    }

    public Iterable<Merchant> getAll() {
        return repository.findAll();
    }
}
