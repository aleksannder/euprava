import {inject} from '@angular/core';
import {HttpInterceptorFn
} from '@angular/common/http';
import {from, switchMap } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {AuthService} from "@auth0/auth0-angular";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);

  return from(auth.getAccessTokenSilently()).pipe(
    switchMap(token => next(token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req)),
    catchError(() => next(req))
  );
}
