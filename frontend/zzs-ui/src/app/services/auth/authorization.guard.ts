import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '@auth0/auth0-angular';
import {map} from 'rxjs';

export const authorizationGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  return auth.isAuthenticated$.pipe(
    map(isAuth => {
      if (isAuth) {
        router.navigate(['/dashboard']);
        return false;
      }
      return true;
    })
  );
};
