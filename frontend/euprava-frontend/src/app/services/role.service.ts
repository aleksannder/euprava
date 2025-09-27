import {Injectable} from "@angular/core";
import {AuthService, User} from "@auth0/auth0-angular";
import {BehaviorSubject, map, Observable} from "rxjs";

@Injectable({ providedIn: 'root' })
export class RoleService {
  constructor (private auth0: AuthService) {
    this.auth0.user$.subscribe(user => {
      const roles: string[] = user?.['https://egov.local/roles'] || [];
      this.roles$.next(roles);
    })
  }
  private roles$ = new BehaviorSubject<string[]>([]);

  get user$(): Observable<User | null | undefined> {
    return this.auth0.user$;
  }

  getRoles$(): Observable<string[]> {
    return this.auth0.user$.pipe(
      map(user => user?.['https://egov.local/roles'] || [])
    );
  }

  hasRole$(role: string): Observable<boolean> {
    return this.getRoles$().pipe(
      map(roles => roles.includes(role))
    );
  }

  hasRole(role: string): boolean {
    return this.roles$.value.includes(role);
  }

  get isAuthenticated$() {
    return this.auth0.isAuthenticated$;
  }
}
