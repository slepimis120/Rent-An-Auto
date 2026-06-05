import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TokenResponse} from "../models/responses/token-response.model";
import {environment} from "../../environment/environment";
import {LoginRequest} from "../models/requests/login-request.model";
import {UserRequest} from "../models/requests/user-request.model";
import {UserResponse} from "../models/responses/user-response.model";
import {CompanyResponse} from "../models/responses/company-response.model";
import {ActivationCodeRequest} from "../models/requests/activation-code-request.model";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  
  private URL_PREFIX: string = `${environment.beUrl}/auth`;

  constructor(
      private httpClient: HttpClient
  ) { }

    public login(request: LoginRequest): Observable<TokenResponse> {
      return this.httpClient.post<TokenResponse>(`${this.URL_PREFIX}/login/`, request);
    }

    public create(request: UserRequest): Observable<UserResponse> {
        return this.httpClient.post<UserResponse>(`${this.URL_PREFIX}/register/`, request);
    }


    private decodeJWT(token: string): any {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        return JSON.parse(jsonPayload);
    }

    public getCompany(token: string): CompanyResponse {
      const decodedToken = this.decodeJWT(token);
      return JSON.parse(decodedToken["company"]);
    }

    public getId(token: string): CompanyResponse {
        const decodedToken = this.decodeJWT(token);
        return JSON.parse(decodedToken["company"]);
    }

    public getRole(token: string): string {
        const decodedToken = this.decodeJWT(token);
        return decodedToken["role"];
    }

    public getEmail(): string | null {
        const token = localStorage.getItem("jwt");
        if (!token) return null;

        const decodedToken = this.decodeJWT(token);
        return decodedToken["sub"] || null;
    }


}
