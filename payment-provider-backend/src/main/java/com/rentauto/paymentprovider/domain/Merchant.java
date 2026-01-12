package com.rentauto.paymentprovider.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DiscriminatorValue("MERCHANT")
public class Merchant extends User{
    @Column(name = "merchant_api_key", unique = true, nullable = false)
    private String merchantApiKey;

    @Column(name = "name")
    private String name;

    @Column(name = "success_url")
    private String successUrl;

    @Column(name = "failed_url")
    private String failedUrl;

    @Column(name = "error_url")
    private String errorUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "merchant_payment_methods", joinColumns = @JoinColumn(name = "merchant_id"))
    @Enumerated(EnumType.STRING)
    private Set<PaymentMethod> enabledPaymentMethods;
}
