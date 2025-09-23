import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from '@auth0/auth0-angular';
import {inject} from '@angular/core';
import {map} from 'rxjs';

export function roleGuard(allowedRoles: string[]): CanActivateFn {
  return (route, state) => {
    const auth = inject(AuthService);
    const router = inject(Router);

    return auth.user$.pipe(
      map(user => {
        const roles: string[] = user?.['https://egov.local/roles'] || [];
        const hasRole = roles.some(r => allowedRoles.includes(r));
        if (hasRole) {
          return true;
        }

        router.navigate(['/unauthorized']);
        return false;
      })
    );
  };
}
