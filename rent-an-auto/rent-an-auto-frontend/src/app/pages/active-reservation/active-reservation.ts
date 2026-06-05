import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { Observable, forkJoin } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-active-reservation',
  standalone: false,
  templateUrl: './active-reservation.html',
  styleUrl: './active-reservation.css',
})
export class ActiveReservation {

  activeReservationsWithVehicle$!: Observable<any[]>;
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

    const today = new Date();

    this.activeReservationsWithVehicle$ = this.http
      .get<any[]>('http://localhost:8000/api/reservations/')
      .pipe(
        map(reservations =>
          reservations.filter(r =>
            r.renter_id.toString() === this.currentUserId.toString() &&
            new Date(r.start_date) > today
          )
        ),
        switchMap(futureReservations =>
          futureReservations.length
            ? forkJoin(
                futureReservations.map(r =>
                  this.http
                    .get<any>(`http://localhost:8000/api/vehicles/${r.vehicle}`)
                    .pipe(
                      map(vehicle => ({
                        ...r,
                        vehicle
                      }))
                    )
                )
              )
            : []
        )
      );
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
