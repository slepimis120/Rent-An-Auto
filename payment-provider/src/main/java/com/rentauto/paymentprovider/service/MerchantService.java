package com.rentauto.paymentprovider.service;

import com.rentauto.paymentprovider.domain.Merchant;
import com.rentauto.paymentprovider.repository.MerchantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MerchantService {
    private final MerchantRepository repository;

    public MerchantService(MerchantRepository repository) {
        this.repository = repository;
    }

    public List<Merchant> getAll(){
        return repository.findAll();
    }

    public Merchant create(Merchant merchant) {
        repository.findByMerchantCode(merchant.getMerchantCode())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Merchant already exists!");
                });
        return repository.save(merchant);
    }

    public Merchant getMerchant(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));
    }

    public Merchant getByMerchantCode(String merchantCode) {
        return repository.findByMerchantCode(merchantCode)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));
    }
}
