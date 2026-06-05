import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
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

  constructor(private http: HttpClient) {}

  ngOnInit() {
    if (this.transactionData && this.transactionData.acquirerTimestamp) {
      this.calculateRemainingTime(this.transactionData.acquirerTimestamp);
    } else {
      this.timerSeconds = 900;
    }
    this.startTimer();
    console.log('Podaci o transakciji primljeni:', this.transactionData);
  }

  calculateRemainingTime(timestamp: string) {
    const startTime = new Date(timestamp).getTime();
    const currentTime = new Date().getTime();
    const diffInSeconds = Math.floor((currentTime - startTime) / 1000);

    const totalSessionDuration = 900;
    this.timerSeconds = totalSessionDuration - diffInSeconds;

    if (this.timerSeconds <= 0) {
      this.timerSeconds = 0;
      this.timeExpired = true;
    }
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

  onPanInput(event: any) {
    let value = event.target.value.replace(/\D/g, '');

    if (value.length > 16) {
      value = value.substring(0, 16);
    }

    const formattedValue = value.match(/.{1,4}/g)?.join(' ') || '';

    this.paymentData.pan = formattedValue;
    event.target.value = formattedValue;

    this.validatePan();
  }

  validatePan() {
    let sum = 0;
    let shouldDouble = false;
    const pan = this.paymentData.pan.replace(/\s+/g, '');

    if (pan.length < 13 || pan.length > 16) {
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

  onExpiryInput(event: any) {
    let value = event.target.value.replace(/\D/g, '');

    if (value.length >= 2) {
      const month = parseInt(value.substring(0, 2));
      if (month < 1 || month > 12) {
        value = value.substring(0, 1);
      }
    }

    let formattedValue = value;
    if (value.length > 2) {
      formattedValue = value.substring(0, 2) + '/' + value.substring(2, 4);
    }

    this.paymentData.expiryDate = formattedValue;
    event.target.value = formattedValue;
  }


  submitPayment() {
    if (this.panInvalid || this.timeExpired) return;

    const payload = {
      ...this.paymentData,
      transactionId: this.transactionData.id
    };

    this.http.post('http://localhost:9090/transactions/pay', payload).subscribe({
      next: (res: any) => {
        window.location.href = res.redirectUrl;
      },
      error: (err) => {
        console.error('Plaćanje nije uspelo:', err);
        alert('Došlo je do greške prilikom obrade kartice.');
      }
    });
  }

  ngOnDestroy() {
    if (this.timerInterval) clearInterval(this.timerInterval);
  }
}
