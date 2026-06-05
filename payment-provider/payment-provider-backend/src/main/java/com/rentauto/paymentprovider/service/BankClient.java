package com.rentauto.paymentprovider.service;

import com.rentauto.paymentprovider.api.dto.TransactionCreateRequest;
import com.rentauto.paymentprovider.api.dto.TransactionCreateResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankClient {

    private final RestTemplate rest = new RestTemplate();

    public TransactionCreateResponse create(TransactionCreateRequest request) {
        return rest.postForObject(
                "http://acquirer-bank:9090/transactions",
                request,
                TransactionCreateResponse.class
        );
    }
}
