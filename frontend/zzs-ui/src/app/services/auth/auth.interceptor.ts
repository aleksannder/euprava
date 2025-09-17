import {from, switchMap} from 'rxjs';
import {inject} from '@angular/core';
import {KeycloakService} from './keycloak.service';
import {HttpInterceptorFn} from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const kc = inject(KeycloakService);
  return from(kc.updateToken(30)).pipe(
    switchMap(() => {
      const token = kc.getToken();
      const authReq = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;
      return next(authReq);
    })
  );
};
