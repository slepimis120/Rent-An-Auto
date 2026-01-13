import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environment/environment';

export type UserRole = 'ROLE_MERCHANT' | 'ROLE_ADMIN';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private tokenKey = 'auth_token';
  private roleKey = 'user_role';
  private email = 'email';
  private isLoggedInSubject = new BehaviorSubject<boolean>(this.hasToken());

  constructor(private http: HttpClient, private router: Router) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post<{ token: string; role: UserRole, email: string; }>(`${environment.pspUrl}/auth/login`, { email, password })
      .pipe(
        tap(res => {
          localStorage.setItem(this.email, res.email);
          localStorage.setItem(this.tokenKey, res.token);
          localStorage.setItem(this.roleKey, res.role);
          this.isLoggedInSubject.next(true);
        })
      );
  }

  register(email: string, password: string) {
    return this.http.post(`${environment.pspUrl}/auth/register`, { email, password }, { responseType: 'text' });
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.roleKey);
    this.isLoggedInSubject.next(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getRole(): UserRole | null {
    return localStorage.getItem(this.roleKey) as UserRole | null;
  }

  isLoggedIn(): Observable<boolean> {
    return this.isLoggedInSubject.asObservable();
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  getEmail() {
    return localStorage.getItem('email') ?? '';
  }
}
