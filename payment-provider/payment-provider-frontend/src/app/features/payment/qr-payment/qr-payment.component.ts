import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { PaymentWsService } from '../../../core/services/payment-ws.service';


@Component({
  selector: 'app-qr-payment',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './qr-payment.component.html',
  styleUrls: ['./qr-payment.component.css']
})
export class QrPaymentComponent implements OnInit {

  @Input() transactionData: any;

  qrCode: string | null = null;
  paymentStatus: string | null = null;

  constructor(
    private http: HttpClient,
    private ws: PaymentWsService
  ) { }

ngOnInit(): void {
  if (!this.transactionData) return;

  this.loadQrCode();

  const paymentId = this.transactionData.id;

  if (paymentId) {
    this.ws.connect(paymentId, (msg) => {

      console.log("DOBIO RESPONSE RBE: " + msg);

      const data = typeof msg === 'string'
        ? JSON.parse(msg)
        : msg;

      this.paymentStatus = data.status;

      if (data.status === 'SUCCESS') {
        setTimeout(() => {
          window.location.href = data.redirectUrl;
        }, 800);
      }
    });
  }
}

  loadQrCode() {
    const segments = this.transactionData.stan.split('-');
    const actualPspStan = segments[6];

    if (actualPspStan) {
      const stan = segments[5] + "-" + segments[6];
      this.http
        .get<any>(`http://localhost:8080/payments/qr/${stan}`)
        .subscribe({
          next: (res) => {
            this.qrCode = res.qrCode;
          },
          error: (err) => {
            console.error('QR loading failed', err);
          }
        });
    } else {
      console.error('Could not extract PSP STAN from bank record');
    }
  }

  simulatePayment() {
    const id = this.transactionData.id;

    this.http.post(`http://localhost:9090/transactions/${id}/qr-pay`, {})
      .subscribe({
        next: () => console.log('Simulated SUCCESS payment sent to bank'),
        error: (err) => console.error(err)
      });
  }

  ngOnDestroy(): void {
    this.ws.disconnect();
  }
}