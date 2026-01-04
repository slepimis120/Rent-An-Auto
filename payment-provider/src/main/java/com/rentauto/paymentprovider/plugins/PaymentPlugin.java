package com.rentauto.paymentprovider.plugins;

import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import com.rentauto.paymentprovider.domain.PaymentMethod;
import com.rentauto.paymentprovider.domain.PaymentStatus;
import com.rentauto.paymentprovider.domain.Transaction;

import java.util.Map;

public interface PaymentPlugin {

    PaymentMethod supports();

    PaymentInitResponse init(Transaction tx);

    void handleCallback(Map<String, String> payload);

    PaymentStatus checkStatus(Transaction tx);
}
