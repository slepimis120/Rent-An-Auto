import { Component } from '@angular/core';

@Component({
  selector: 'app-payment-error',
  standalone: false,
  templateUrl: './payment-error.html',
  styleUrl: './payment-error.css',
})
export class PaymentError {
  goHome() {
    window.location.href = '/home';
  }
}
