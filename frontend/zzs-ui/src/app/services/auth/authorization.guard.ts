import {CanActivateFn, Router} from '@angular/router';
import {AuthTokenUtil} from './auth-token.util';
import {inject} from '@angular/core';
import {Role} from '../../model/register-user.model';
import {map} from 'rxjs';

export function roleGuard(requiredRole: string): CanActivateFn {
  return (route, state) => {
    const authTokenUtil = inject(AuthTokenUtil);
    const router = inject(Router);

    return authTokenUtil.hasPermission(requiredRole).pipe(
      map(hasRole => {
        if (!hasRole) {
          console.log(hasRole);
          router.navigate(['/unauthorized']);
          return false;
        }
        return true;
      })
    );
  };
}

export const citizenGuard = roleGuard('citizen:access');

export const analystGuard = roleGuard('zzs:analyst');

export const adminGuard = roleGuard('zzs:admin');

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthTokenUtil);
  const router = inject(Router);

  return auth.isAuthenticated().pipe(
    map(isAuth => {
      if (!isAuth) {
        router.navigate(['/login']);
        return false;
      }

      return true;
    })
  );
};
