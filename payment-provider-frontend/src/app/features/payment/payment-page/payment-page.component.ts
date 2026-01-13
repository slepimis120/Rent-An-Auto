import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import {CardPaymentComponent} from '../card-payment/card-payment.component';

@Component({
  selector: 'app-payment-page',
  standalone: true,
  imports: [CommonModule, FormsModule, CardPaymentComponent],
  templateUrl: './payment-page.component.html',
  styleUrls: ['./payment-page.component.css']
})
export class PaymentPageComponent implements OnInit {
  transaction: any = null;
  pspData: any = null;

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
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.http.get(`http://localhost:9090/transactions/${id}`).subscribe({
        next: (bankTx: any) => {
          this.transaction = bankTx;

          const segments = bankTx.stan.split('-');
          const actualPspStan = segments[6];

          if (actualPspStan) {
            this.fetchPspMethod(actualPspStan);
          } else {
            console.error('Could not extract PSP STAN from bank record');
            this.errorMessage = 'Invalid transaction format';
            this.loading = false;
          }
        },
        error: (err) => {
          this.errorMessage = 'Transaction not found in Bank records';
          this.loading = false;
        }
      });
    }
  }

  fetchPspMethod(stan: string) {
    this.http.get(`http://localhost:8080/payments/type/${stan}`).subscribe({
      next: (res: any) => {
        this.pspData = res;
        this.loading = false;
      },
      error: (err) => {
        console.warn('PSP endpoint failed, using dummy CARD data');
        this.pspData = { paymentMethod: 'CARD' };
        this.loading = false;
      }
    });
  }
}
