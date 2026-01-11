package com.rentanauto.acquirerbank.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TRANSACTIONS")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @Column(name = "STAN", unique = true)
    private String stan;

    @Column(name = "MERCHANT_ID", nullable = false)
    private UUID merchantId;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "CURRENCY", nullable = false)
    private String currency;

    @Column(name = "PSP_TIMESTAMP")
    private String pspTimestamp;

    @Column(name = "ACQUIRER_TIMESTAMP")
    private String acquirerTimestamp;

    @Column(name = "PAYMENT_URL")
    private String paymentUrl;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
