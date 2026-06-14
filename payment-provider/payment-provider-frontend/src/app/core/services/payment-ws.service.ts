import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';

@Injectable({ providedIn: 'root' })
export class PaymentWsService {

  private client!: Client;

  connect(paymentId: string, callback: (msg: any) => void) {

    this.client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      reconnectDelay: 5000
    });

  this.client.onConnect = () => {

    console.log('WS CONNECTED');

    this.client.subscribe(
      `/topic/payment/${paymentId}`,
      message => {
        console.log('WS MESSAGE:', message.body);
        callback(message.body);
      }
    );

  };

  this.client.onStompError = (frame) => {
    console.error('STOMP ERROR', frame);
  };

  this.client.onWebSocketError = (event) => {
    console.error('WEBSOCKET ERROR', event);
  };

  this.client.onDisconnect = () => {
    console.log('WS DISCONNECTED');
  };

    this.client.activate();
  }

  disconnect() {
    this.client?.deactivate();
  }
}