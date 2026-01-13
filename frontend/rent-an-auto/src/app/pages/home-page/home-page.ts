import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject, combineLatest } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.html',
  standalone: false,
  styleUrls: ['./home-page.css']
})
export class HomePage implements OnInit {

  private vehicles$ = new BehaviorSubject<any[]>([]);
  private reservations$ = new BehaviorSubject<any[]>([]);

  search$ = new BehaviorSubject<string>('');
  city$ = new BehaviorSubject<string>('');
  brand$ = new BehaviorSubject<string>('');
  ordering$ = new BehaviorSubject<string>('');
  startDate$ = new BehaviorSubject<string>('');
  endDate$ = new BehaviorSubject<string>('');

  filteredVehicles$ = combineLatest([
    this.vehicles$,
    this.reservations$,
    this.search$,
    this.city$,
    this.brand$,
    this.ordering$,
    this.startDate$,
    this.endDate$
  ]).pipe(
    map(([vehicles, reservations, search, city, brand, ordering, startDate, endDate]) => {
      let filtered = [...vehicles];

      if (search) {
        filtered = filtered.filter(v =>
          v.brand.toLowerCase().includes(search.toLowerCase()) ||
          v.model.toLowerCase().includes(search.toLowerCase())
        );
      }

      if (city) {
        filtered = filtered.filter(v =>
          v.city.toLowerCase().includes(city.toLowerCase())
        );
      }

      if (brand) {
        filtered = filtered.filter(v =>
          v.brand.toLowerCase().includes(brand.toLowerCase())
        );
      }

      if (ordering) {
        const field = ordering.replace('-', '');
        const dir = ordering.startsWith('-') ? -1 : 1;
        filtered = filtered.sort((a, b) =>
          a[field] > b[field] ? dir : -dir
        );
      }

      if (startDate && endDate) {
        const start = new Date(startDate);
        const end = new Date(endDate);

        filtered = filtered.filter(v =>
          !reservations.some(r =>
            r.vehicle === v.id &&
            start <= new Date(r.end_date) &&
            end >= new Date(r.start_date)
          )
        );
      }

      return filtered;
    })
  );

  showPaymentModal = false;
  selectedVehicle: any;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    const token = localStorage.getItem('access');
    if (!token) return;

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    this.http
      .get<any[]>('http://127.0.0.1:8000/api/vehicles/', { headers })
      .subscribe(v => this.vehicles$.next(v));

    this.http
      .get<any[]>('http://127.0.0.1:8000/api/reservations/', { headers })
      .subscribe(r => this.reservations$.next(r));
  }

  openPayment(vehicle: any) {
    this.selectedVehicle = vehicle;
    this.showPaymentModal = true;
  }

  closePayment() {
    this.showPaymentModal = false;
  }

  pay(method: 'CARD' | 'QR') {
    const body = {
      merchantId: 'fb440d26-f064-11f0-b818-b262890194ad',
      merchantApiKey: 'GqcexvV7aTS0ekMCoULOOovruWz9S3eE',
      merchantOrderId: 'ORDER-' + crypto.randomUUID(),
      merchantTimestamp: new Date().toISOString(),
      amount: this.selectedVehicle.price_per_day,
      currency: 'EUR',
      paymentMethod: method
    };

    this.http
      .post<any>('http://localhost:8080/payments/card/process', body)
      .subscribe(res => {
        window.location.href = res.paymentUrl;
      });
  }

  logout() {
    localStorage.clear();
    window.location.href = '/login';
  }

  history() {
    window.location.href = '/history';
  }

  activeReservations() {
    window.location.href = '/active';
  }

  home() {
    window.location.href = '/home';
  }
}
