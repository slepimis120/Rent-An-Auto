import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

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

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    if (!this.transactionData) return;

    this.loadQrCode();
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
}