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
    @Column(unique = true, nullable = false)
    private String code;

    private String name;
    private String successUrl;
    private String failedUrl;
    private String errorUrl;

    @ElementCollection(targetClass = PaymentMethod.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "merchant_payment_methods", joinColumns = @JoinColumn(name = "merchant_id"))
    @Enumerated(EnumType.STRING)
    private Set<PaymentMethod> enabledPaymentMethods;
}
