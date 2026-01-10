import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import {HomeMerchantComponent} from './features/home-merchant/home-merchant.component';
import {merchantGuard} from './core/guards/merchant.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'merchant',
    component: HomeMerchantComponent,
    canActivate: [merchantGuard]
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];

