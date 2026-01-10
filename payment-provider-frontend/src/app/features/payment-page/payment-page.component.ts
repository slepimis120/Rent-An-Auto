import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import {environment} from '../../../environment/environment';

interface Transaction {
  stan: string;
  amount: number;
  currency: string;
  paymentMethod: 'CARD' | 'QR_CODE' | 'PAYPAL' | 'CRYPTO';
  merchantOrderId: string;
  merchantCode: string;
  paymentUrl?: string;
  qrCode?: string;
}

@Component({
  selector: 'app-payment-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './payment-page.component.html',
  styleUrls: ['./payment-page.component.css']
})
export class PaymentPageComponent implements OnInit {

  transaction!: Transaction;
  loading = true;
  errorMessage = '';

  cardData = {
    pan: '',
    securityCode: '',
    cardHolder: '',
    expiry: ''
  };

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const stan = this.route.snapshot.paramMap.get('stan');
    if (stan) {
      this.http.get<Transaction>(`${environment.apiUrl}/payments/status/${stan}`).subscribe({
        next: (tx) => {
          this.transaction = tx;
          this.loading = false;
        },
        error: (err) => {
          this.errorMessage = 'Transaction not found';
          this.loading = false;
        }
      });
    }
  }

  submitCardPayment(form: NgForm) {
    if (form.invalid) return;
    console.log('CARD Payment submitted', this.cardData, this.transaction);
  }

}
