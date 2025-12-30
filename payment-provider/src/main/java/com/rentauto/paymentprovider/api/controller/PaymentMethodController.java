package com.rentauto.paymentprovider.api.controller;

import com.rentauto.paymentprovider.domain.PaymentMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/payment-methods")
public class PaymentMethodController {
    @GetMapping
    public List<PaymentMethod> getAllPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }
}
