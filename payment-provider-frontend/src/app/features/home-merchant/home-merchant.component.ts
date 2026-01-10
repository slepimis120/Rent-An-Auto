import { Component, OnInit } from '@angular/core';
import { MerchantService, Merchant } from '../../core/services/merchant.service';
import { AuthService } from '../../core/services/auth.service';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-home-merchant',
  imports: [FormsModule, CommonModule],
  standalone: true,
  templateUrl: './home-merchant.component.html',
  styleUrls: ['./home-merchant.component.css']
})
export class HomeMerchantComponent implements OnInit {

  merchant: Merchant = {
    code: '',
    name: '',
    successUrl: '',
    failedUrl: '',
    errorUrl: '',
    enabledPaymentMethods: [],
    email: '',
    role: ''
  };
  email: string = '';

  constructor(
    private merchantService: MerchantService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.email = this.authService.getEmail();
    this.loadMerchantData();
  }

  loadMerchantData() {
    this.merchantService.getMerchantByEmail(this.email).subscribe(res => {
      this.merchant = {
        ...res,
        enabledPaymentMethods: res.enabledPaymentMethods ?? []
      };
    });
  }

  save() {
    if (!this.merchant) return;

    this.merchantService.updateMerchant(this.email, this.merchant).subscribe(() => {
      alert('Saved successfully!');
    });
  }

  logout() {
    this.authService.logout();
  }

  togglePM(method: string, event: any) {
    if (!this.merchant) return;

    if (event.target.checked) {
      if (!this.merchant.enabledPaymentMethods.includes(method)) {
        this.merchant.enabledPaymentMethods.push(method);
      }
    } else {
      this.merchant.enabledPaymentMethods =
        this.merchant.enabledPaymentMethods.filter(m => m !== method);
    }
  }

}
