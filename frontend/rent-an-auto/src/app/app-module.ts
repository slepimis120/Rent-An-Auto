import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { LoginPage } from './pages/login-page/login-page';
import { RegisterPage } from './pages/register-page/register-page';
import { HomePage } from './pages/home-page/home-page';
import {JwtInterceptor} from "./interceptors/jwt.interceptor";
import {FormsModule} from "@angular/forms";
import { CommonModule } from '@angular/common';
import { ReservationHistory } from './pages/reservation-history/reservation-history';
import { ActiveReservation } from './pages/active-reservation/active-reservation';
import { PaymentSuccess } from './pages/payment-success/payment-success';
import { PaymentFailed } from './pages/payment-failed/payment-failed';
import { PaymentError } from './pages/payment-error/payment-error';


@NgModule({
  declarations: [
    App,
    LoginPage,
    RegisterPage,
    HomePage,
    ReservationHistory,
    ActiveReservation,
    PaymentSuccess,
    PaymentFailed,
    PaymentError
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    CommonModule, 
    FormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    provideBrowserGlobalErrorListeners(),
    provideClientHydration(withEventReplay()),
  ],
  bootstrap: [App]
})
export class AppModule { }
