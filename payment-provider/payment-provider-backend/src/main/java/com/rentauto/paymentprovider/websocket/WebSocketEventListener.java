package com.rentauto.paymentprovider.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger log =
        LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleConnected(SessionConnectedEvent event) {
        log.info("WS connected");
    }

    @EventListener
    public void handleDisconnected(SessionDisconnectEvent event) {
        log.info("WS disconnected");
    }
}