import { Component } from '@angular/core';

@Component({
  selector: 'app-payment-failed',
  standalone: false,
  templateUrl: './payment-failed.html',
  styleUrl: './payment-failed.css',
})
export class PaymentFailed {
  
  retry() {
    window.location.href = '/home';
  }
}
