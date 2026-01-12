package com.rentanauto.acquirerbank.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "merchants")
@Getter
@Setter
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID merchantId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "merchant_api_key", nullable = false, unique = true)
    private String merchantApiKey;

    @Column(name = "is_active")
    private boolean isActive;
}
