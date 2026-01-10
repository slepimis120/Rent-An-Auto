import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import {HomeMerchantComponent} from './features/home-merchant/home-merchant.component';
import {merchantGuard} from './core/guards/merchant.guard';
import {PaymentPageComponent} from './features/payment-page/payment-page.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'merchant',
    component: HomeMerchantComponent,
    canActivate: [merchantGuard]
  },
  { path: 'pay/:stan', component: PaymentPageComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];

