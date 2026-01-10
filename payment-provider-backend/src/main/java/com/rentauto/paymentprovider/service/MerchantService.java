package com.rentauto.paymentprovider.service;

import com.rentauto.paymentprovider.api.dto.RegisterRequest;
import com.rentauto.paymentprovider.domain.Merchant;
import com.rentauto.paymentprovider.domain.User;
import com.rentauto.paymentprovider.repository.MerchantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MerchantService {
    private final MerchantRepository repository;
    private final PasswordEncoder passwordEncoder;

    public MerchantService(MerchantRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Merchant> getAll(){
        return repository.findAll();
    }

    public Merchant create(RegisterRequest request) {

        repository.findByEmail(request.email()).ifPresent(existing -> {
            throw new IllegalArgumentException("Merchant with email already exists!");
        });

        Merchant merchant = new Merchant();
        merchant.setEmail(request.email());
        merchant.setPassword(passwordEncoder.encode(request.password()));
        merchant.setCode(generateMerchantCode());
        merchant.setRole(User.Role.ROLE_MERCHANT);

        return repository.save(merchant);
    }

    public Merchant update(Merchant updatedMerchant) {
        Merchant existingMerchant = repository.findById(updatedMerchant.getId())
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));

        existingMerchant.setEmail(updatedMerchant.getEmail());
        if (updatedMerchant.getPassword() != null && !updatedMerchant.getPassword().isEmpty()) {
            existingMerchant.setPassword(passwordEncoder.encode(updatedMerchant.getPassword()));
        }

        if(updatedMerchant.getEmail() != null){
            repository.findByEmail(updatedMerchant.getEmail()).ifPresent(merchantWithEmail -> {
                if(!merchantWithEmail.getId().equals(existingMerchant.getId())){
                    throw new IllegalArgumentException("Merchant with email already exists!");
                }
            });
        }

        if(updatedMerchant.getPassword() != null){
            existingMerchant.setPassword(passwordEncoder.encode(updatedMerchant.getPassword()));
        }

        if(updatedMerchant.getName() != null){
            existingMerchant.setName(updatedMerchant.getName());
        }

        if(updatedMerchant.getSuccessUrl() != null){
            existingMerchant.setSuccessUrl(updatedMerchant.getSuccessUrl());
        }

        if(updatedMerchant.getFailedUrl() != null){
            existingMerchant.setFailedUrl(updatedMerchant.getFailedUrl());
        }

        if(updatedMerchant.getErrorUrl() != null){
            existingMerchant.setErrorUrl(updatedMerchant.getErrorUrl());
        }

        if(updatedMerchant.getEnabledPaymentMethods() != null) {
            existingMerchant.getEnabledPaymentMethods().clear();
            existingMerchant.getEnabledPaymentMethods().addAll(updatedMerchant.getEnabledPaymentMethods());
        }


        return repository.save(existingMerchant);
    }

    public Merchant getMerchant(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));
    }

    public Merchant getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));
    }

    public Merchant getByMerchantCode(String merchantCode) {
        return repository.findByCode(merchantCode)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));
    }

    private String generateMerchantCode() {
        return "MER-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
