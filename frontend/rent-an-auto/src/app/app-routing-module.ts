import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginPage} from "./pages/login-page/login-page";
import {RegisterPage} from "./pages/register-page/register-page";
import { HomePage } from './pages/home-page/home-page';
import { ActiveReservation } from './pages/active-reservation/active-reservation';
import { ReservationHistory } from './pages/reservation-history/reservation-history';

const routes: Routes = [
  { path: 'login', component: LoginPage },
  { path: 'register', component: RegisterPage },
  { path: 'home', component: HomePage },
  { path: 'active', component: ActiveReservation },
  { path: 'history', component: ReservationHistory },
  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
