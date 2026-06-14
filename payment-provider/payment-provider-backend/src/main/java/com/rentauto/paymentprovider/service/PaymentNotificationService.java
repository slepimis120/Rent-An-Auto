package com.rentauto.paymentprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyPaymentStatus(String paymentId, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/payment/" + paymentId,
                payload
        );
    }
}