package com.rentanauto.acquirerbank.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentanauto.acquirerbank.domain.CardHolder;

@Repository
public interface CardHolderRepository
        extends JpaRepository<CardHolder, UUID> {

    Optional<CardHolder> findByPanEncrypted(String panEncrypted);
}
