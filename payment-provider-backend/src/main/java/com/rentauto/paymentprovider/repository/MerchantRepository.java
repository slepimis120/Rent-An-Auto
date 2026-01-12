package com.rentauto.paymentprovider.repository;

import com.rentauto.paymentprovider.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    Optional<Merchant> findByEmail(String email);
    Optional<Merchant> findByMerchantApiKey(String merchantApiKey);
}
