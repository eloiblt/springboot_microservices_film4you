import { isPlatformBrowser } from '@angular/common';
import { HttpRequest, HttpErrorResponse, HttpHandler, HttpEvent } from '@angular/common/http';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
import { throwError, Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { SocialUser } from 'angularx-social-login';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService {

  constructor(
    private toastr: ToastrService,
    private userService: UserService,
    @Inject(PLATFORM_ID) private platformId: unknown,
  ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(this.injectToken(request)).pipe(catchError(this.handleError.bind(this)));
  }

  injectToken(request: HttpRequest<any>) {
    const token = this.userService.getToken();

    if (!token) { return request; }

    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  handleError(error: HttpErrorResponse) {
    if (error instanceof HttpErrorResponse) {
      if (isPlatformBrowser(this.platformId) && error.error instanceof ErrorEvent) {
      } else {
        switch (error.status) {
          case 401:
            this.userService.signout();
            return of();
          case 403:
            this.userService.signout();
            return of();
          case 404:
            return of();
        }
      }
    } else {
      this.toastr.error('Internal error');
    }

    return throwError(() => new Error());
  }
}
