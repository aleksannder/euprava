import {Injectable} from '@angular/core';
import {AuthService} from '@auth0/auth0-angular';
import {map, Observable} from 'rxjs';

@Injectable(
  {providedIn: 'root'}
)
export class AuthTokenUtil {

  constructor(private auth: AuthService) {}

  getAccessToken(): Observable<string | null> {
    return this.auth.getAccessTokenSilently().pipe(
      map(token => token || null)
    );
  }

  getDecodedToken(): Observable<any> {
    return this.getAccessToken().pipe(
      map(token => {
        if (!token) return null;
        try {
          const payload = token.split('.')[1];
          return JSON.parse(atob(payload));
        } catch (e) {
          console.error('Failed to decode token', e);
          return null;
        }
      })
    );
  }

  hasRole(role: string): Observable<boolean> {
    return this.getDecodedToken().pipe(
      map(decoded => {
        console.log(decoded);
        if (!decoded || !decoded['permissions']) return false;
        return decoded['permissions'].includes(role);
      })
    );
  }

  hasPermission(permission: string): Observable<boolean> {
    return this.getDecodedToken().pipe(
      map(decoded => {
        if (!decoded || !decoded['permissions']) return false;
        return decoded['permissions'].includes(permission);
      })
    );
  }

  getUserProfile(): Observable<any> {
    return this.auth.user$;
  }

  isAuthenticated(): Observable<boolean> {
    return this.auth.isAuthenticated$;
  }
}
