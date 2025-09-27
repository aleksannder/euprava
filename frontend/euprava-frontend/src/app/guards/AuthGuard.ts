import {CanActivateFn, Router} from "@angular/router";
import {inject} from "@angular/core";
import {AuthService} from "@auth0/auth0-angular";
import {map} from "rxjs";


export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  return auth.isAuthenticated$.pipe(
    map(isAuthenticated => {
      if (!isAuthenticated) {
        router.navigate(['/login'])
        return false;
      }
      return true;
    })
  );
};

export function roleGuard(allowedRoles: string[]): CanActivateFn {
  const auth = inject(AuthService);
  const router = inject(Router);
  return (route, state) => {
    return auth.user$.pipe(
      map(user => {
        const roles: string[] = user?.['https://egov.local/roles'] || [];
        const hasRole = roles.some(r => allowedRoles.includes(r));

        if (hasRole) return true;

        router.navigate(['/custom-alert']);
        return false;
      })
    );
  };
}
