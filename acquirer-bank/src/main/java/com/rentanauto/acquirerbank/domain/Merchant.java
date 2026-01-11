package com.rentanauto.acquirerbank.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "MERCHANTS")
@Getter
@Setter
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID merchantId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ACQUIRER_CONTRACT_ID", nullable = false)
    private UUID acquirerContractId;

    private boolean isActive;
}
