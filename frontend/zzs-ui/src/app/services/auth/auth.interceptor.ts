import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {catchError, from, switchMap} from 'rxjs';
import {AuthService} from "@auth0/auth0-angular";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);

  return from(auth.getAccessTokenSilently()).pipe(
    switchMap(token => next(token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req)),
    catchError(() => next(req)) // never block requests if token refresh fails
  );
};
