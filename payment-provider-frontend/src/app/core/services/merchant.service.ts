import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environment/environment';

export interface Merchant {
  id: string;
  name: string;
  successUrl: string;
  failedUrl: string;
  errorUrl: string;
  enabledPaymentMethods: string[];
  email: string;
  role: string;
  merchantApiKey: string;
}

@Injectable({
  providedIn: 'root'
})
export class MerchantService {

  constructor(private http: HttpClient) {}

  getMerchantByEmail(email: string): Observable<Merchant> {
    return this.http.get<Merchant>(`${environment.apiUrl}/merchants/email/${email}`);
  }

  updateMerchant(email: string, payload: Merchant): Observable<any> {
    return this.http.post(`${environment.apiUrl}/merchants`, payload);
  }
}
