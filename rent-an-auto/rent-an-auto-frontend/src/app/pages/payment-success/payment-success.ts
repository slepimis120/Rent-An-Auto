import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-payment-success',
  standalone: false,
  templateUrl: './payment-success.html',
  styleUrl: './payment-success.css',
})
export class PaymentSuccess implements OnInit {

  paymentId!: string | null;
  payment: any;
  loading = true;
  reservationCreated = false;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.paymentId = this.route.snapshot.queryParamMap.get('id');
    if (!this.paymentId) return;

    this.loadPayment();
  }

  loadPayment() {
    this.http
      .get<any>(`http://localhost:8080/payments/external/${this.paymentId}`)
      .subscribe(res => {
        this.payment = res;
        this.loading = false;

        this.cdr.detectChanges();

        if (this.payment.paymentStatus === 'SUCCESS') {
          this.createReservation();
        }
      });
  }

  createReservation() {
    const reservationData = JSON.parse(
      localStorage.getItem('pendingReservation') || '{}'
    );

    if (!reservationData.vehicle) return;

    const token = localStorage.getItem('access');
    if (!token) return;

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    this.http
      .post(
        'http://localhost:8080/api/reservations/',
        reservationData,
        { headers }
      )
      .subscribe(() => {
        this.reservationCreated = true;
        localStorage.removeItem('pendingReservation');
        this.cdr.detectChanges();
      });
  }

  goHome() {
    window.location.href = '/home';
  }
}
