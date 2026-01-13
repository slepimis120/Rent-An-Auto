import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-reservation-history',
  standalone: false,
  templateUrl: './reservation-history.html',
  styleUrls: ['./reservation-history.css'],
})
export class ReservationHistory implements OnInit {

  reservations: any[] = [];
  userReservations: any[] = [];
  reservationsWithVehicle: any[] = [];
  currentUserId!: number;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    const userId = this.getUserIdFromToken();
    if (!userId) return;

    this.currentUserId = userId;

    this.loadReservations();
  }

  getUserIdFromToken(): number | null {
    const token = localStorage.getItem('access');
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.user_id;
    } catch {
      return null;
    }
  }

  loadReservations() {
    this.http
      .get<any[]>('http://localhost:8000/api/reservations/')
      .subscribe(async res => {
        this.reservations = res;
        this.filterByUser();
        await this.loadVehicleDetails();
      });
  }

  filterByUser() {
    this.userReservations = this.reservations.filter(
      r => r.renter_id.toString() === this.currentUserId.toString()
    );
  }

  isPast(r: any): boolean {
    return new Date(r.end_date) < new Date();
  }

  isActive(r: any): boolean {
    const today = new Date();
    return new Date(r.start_date) <= today && new Date(r.end_date) >= today;
  }

  async loadVehicleDetails() {
    const vehicleRequests = this.userReservations.map(r =>
      lastValueFrom(this.http.get<any>(`http://localhost:8000/api/vehicles/${r.vehicle_id}`))
    );

    try {
      const vehicles = await Promise.all(vehicleRequests);
      this.reservationsWithVehicle = this.userReservations.map((r, i) => ({
        ...r,
        vehicle: vehicles[i]
      }));
    } catch (err) {
      console.error('Error loading vehicles', err);
    }
  }

  logout() {
    localStorage.clear();
    window.location.href = '/login';
  }

  home() {
    window.location.href = '/home';
  }

  history() {
    window.location.href = '/history';
  }

  activeReservations() {
    window.location.href = '/active';
  }
}
