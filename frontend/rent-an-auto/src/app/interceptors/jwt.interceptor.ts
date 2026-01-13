import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../environment/environment';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {

    // ⛔ SERVER → NE DIRAJ localStorage
    if (!isPlatformBrowser(this.platformId)) {
      return next.handle(request);
    }

    const token = localStorage.getItem(environment.jwtKeyName);

    const excludedUrls = ['/auth'];

    if (excludedUrls.some(url => request.url.includes(url))) {
      return next.handle(request);
    }

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request);
  }
}
