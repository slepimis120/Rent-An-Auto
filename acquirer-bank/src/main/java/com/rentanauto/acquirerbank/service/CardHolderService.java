package com.rentanauto.acquirerbank.service;

import org.springframework.stereotype.Service;

import com.rentanauto.acquirerbank.domain.CardHolder;
import com.rentanauto.acquirerbank.repository.CardHolderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardHolderService {

    private final CardHolderRepository repository;

    public CardHolder findByPan(String pan) {
        return repository.findByPan(pan)
                .orElseThrow(() ->
                        new IllegalArgumentException("Card not found"));
    }
}
