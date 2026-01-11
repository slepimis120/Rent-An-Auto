import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {LoginRequest} from "../../models/requests/login-request.model";
import {HttpErrorResponse} from "@angular/common/http";
import {environment} from "../../../environment/environment";
import { Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';


@Component({
  selector: 'app-login-page',
  standalone: false,
  templateUrl: './login-page.html',
  styleUrl: './login-page.css',
})
export class LoginPage implements OnInit {

    loginRequest: LoginRequest = {} as LoginRequest;
    errorMessage: string | undefined;
  
    constructor(
      protected router: Router,
      private authService: AuthService,
      @Inject(PLATFORM_ID) private platformId: Object
    ) {}
  
    async login() {
      try {
        const response = await this.authService.login(this.loginRequest).toPromise();
        if (response && isPlatformBrowser(this.platformId)) {
          localStorage.setItem("access", response.access);
          this.router.navigate(["home"]);
        }
      } catch (error) {
        if (error instanceof HttpErrorResponse) {
          this.errorMessage = error.error;
        }
      }
    }
  
    ngOnInit(): void {
      if (isPlatformBrowser(this.platformId)) {
        localStorage.removeItem(environment.jwtKeyName);
      }
    }
  }
  