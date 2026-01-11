import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';


@Component({
  selector: 'app-home-page',
  standalone: false,
  templateUrl: './home-page.html',
  styleUrls: ['./home-page.css']
})
export class HomePage implements OnInit {

  vehicles: Vehicle[] = [];
  filteredVehicles: Vehicle[] = [];
  reservations: Reservation[] = [];

  search = '';
  city = '';
  brand = '';
  ordering = '';

  startDate = '';
  endDate = '';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadData();
    }
  }

  loadData() {
    if (!isPlatformBrowser(this.platformId)) return;

    const token = localStorage.getItem('access');
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    // 1️⃣ Dohvati vozila
    this.http.get<Vehicle[]>('http://127.0.0.1:8000/api/vehicles/', { headers }).subscribe(vehicles => {
      this.vehicles = vehicles;

      // 2️⃣ Dohvati sve rezervacije
      this.http.get<Reservation[]>('http://127.0.0.1:8000/api/reservations/', { headers }).subscribe(res => {
        this.reservations = res;

        // 3️⃣ Primeni filtere (search, city, brand, ordering i dostupnost)
        this.applyFilters();
      });
    });
  }

  applyFilters() {
    let filtered = [...this.vehicles];

    // Search/filter/sort
    if (this.search) {
      filtered = filtered.filter(v =>
        v.brand.toLowerCase().includes(this.search.toLowerCase()) ||
        v.model.toLowerCase().includes(this.search.toLowerCase())
      );
    }
    if (this.city) {
      filtered = filtered.filter(v => v.city.toLowerCase().includes(this.city.toLowerCase()));
    }
    if (this.brand) {
      filtered = filtered.filter(v => v.brand.toLowerCase().includes(this.brand.toLowerCase()));
    }
    if (this.ordering) {
      filtered.sort((a, b) => {
        const field = this.ordering.replace('-', '');
        const dir = this.ordering.startsWith('-') ? -1 : 1;
        return (a[field as keyof Vehicle] as any) > (b[field as keyof Vehicle] as any) ? 1 * dir : -1 * dir;
      });
    }

    // Filter po dostupnosti
    if (this.startDate && this.endDate) {
      const start = new Date(this.startDate);
      const end = new Date(this.endDate);

      filtered = filtered.filter(v => 
        !this.reservations.some(r => r.vehicle === v.id && start <= new Date(r.end_date) && end >= new Date(r.start_date))
      );
    }

    this.filteredVehicles = filtered;
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.clear();
      window.location.href = '/login';
    }
  }
}
