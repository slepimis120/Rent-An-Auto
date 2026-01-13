import {Component, OnInit, OnDestroy, Input} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-card-payment',
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule],
  templateUrl: './card-payment.component.html',
  styleUrl: './card-payment.component.css'
})
export class CardPaymentComponent implements OnInit, OnDestroy {
  @Input() transactionData: any;
  transaction: any = null;
  paymentData = {
    cardHolderName: '',
    pan: '',
    expiryDate: '',
    securityCode: ''
  };


  panInvalid = false;
  timeExpired = false;
  timeLeft = '15:00';
  private timerSeconds = 900;
  private timerInterval: any;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    const txId = this.route.snapshot.paramMap.get('id');
    //this.fetchTransaction(txId);
    this.startTimer();
  }

  fetchTransaction(id: string | null) {
    if (!id) return;
    this.http.get(`http://localhost:8081/api/transactions/${id}`).subscribe({
      next: (res) => this.transaction = res,
      error: (err) => console.error('Transaction not found', err)
    });
  }

  startTimer() {
    this.timerInterval = setInterval(() => {
      this.timerSeconds--;
      const mins = Math.floor(this.timerSeconds / 60);
      const secs = this.timerSeconds % 60;
      this.timeLeft = `${mins}:${secs < 10 ? '0' : ''}${secs}`;

      if (this.timerSeconds <= 0) {
        this.timeExpired = true;
        clearInterval(this.timerInterval);
      }
    }, 1000);
  }

  validatePan() {
    let sum = 0;
    let shouldDouble = false;
    const pan = this.paymentData.pan.replace(/\s+/g, '');

    if (pan.length < 13) {
      this.panInvalid = true;
      return;
    }

    for (let i = pan.length - 1; i >= 0; i--) {
      let digit = parseInt(pan.charAt(i));
      if (shouldDouble) {
        digit *= 2;
        if (digit > 9) digit -= 9;
      }
      sum += digit;
      shouldDouble = !shouldDouble;
    }
    this.panInvalid = !(sum % 10 === 0);
  }

  submitPayment() {
    if (this.panInvalid || this.timeExpired) return;

    const payload = {
      ...this.paymentData,
      transactionId: this.transaction.id
    };

    this.http.post('http://localhost:8081/api/transactions/pay', payload).subscribe({
      next: (res: any) => {
        window.location.href = res.redirectUrl;
      },
      error: (err) => {
        alert('Payment failed or error occurred');
      }
    });
  }

  ngOnDestroy() {
    if (this.timerInterval) clearInterval(this.timerInterval);
  }
}
