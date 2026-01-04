package com.rentauto.paymentprovider.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "merchants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Merchant {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String password;

    private String name;
    private String successUrl;
    private String failedUrl;
    private String errorUrl;

    @ElementCollection(targetClass = PaymentMethod.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "merchant_payment_methods", joinColumns = @JoinColumn(name = "merchant_id"))
    @Enumerated(EnumType.STRING)
    private Set<PaymentMethod> enabledPaymentMethods;
}
