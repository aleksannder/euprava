import {Injectable} from '@angular/core';
import Keycloak from 'keycloak-js';


@Injectable({
  providedIn: 'root',
})
export class KeycloakService {
  private keycloakInstance!: Keycloak;

  init(): Promise<boolean> {
    this.keycloakInstance = new Keycloak({
      url: 'http://localhost:8081',
      realm: 'gov-realm',
      clientId: 'zzs-ui'
    });

    return this.keycloakInstance
      .init({
        onLoad: 'check-sso',
        pkceMethod: 'S256',
        silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
      })
      .then(auth => {
        console.info('Init keycloak success, authenticated = ', auth);
        return auth;
      })
      .catch(err => {
        console.error('Keycloak init failed ', err);
        return false;
      });
  }

  login(): void {
    this.keycloakInstance.login();
  }

  logout(): void {
    this.keycloakInstance.logout();
  }

  isAuthenticated(): boolean {
    return !!this.keycloakInstance?.authenticated;
  }

  getToken(): string | undefined {
    return this.keycloakInstance?.token;
  }

  getEmail(): string {
    return this.keycloakInstance?.tokenParsed?.['email'] ?? '';
  }

  getRoles(): string[] {
    const realmRoles = this.keycloakInstance.realmAccess?.roles || [];
    const clientRoles =
      this.keycloakInstance.resourceAccess?.[this.keycloakInstance.clientId || '']?.roles || [];
    return [...realmRoles, ...clientRoles];
  }

  updateToken(minValidity = 30): Promise<boolean> {
    return this.keycloakInstance.updateToken(minValidity);
  }
}
