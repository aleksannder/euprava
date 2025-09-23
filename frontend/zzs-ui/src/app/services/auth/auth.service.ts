import {Auth0Client, createAuth0Client} from '@auth0/auth0-spa-js';
import {Injectable} from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private client?: Auth0Client;

  async init(domain: string, clientId: string, audience: string): Promise<void> {
    this.client = await createAuth0Client({
      domain,
      clientId,
      authorizationParams: {
        redirect_uri: window.location.origin,
        audience,
        scope: 'openid profile email'
      },
      cacheLocation: 'localstorage',
      useRefreshTokens: true
    });

    if (window.location.search.includes('code=')) {
      await this.client.handleRedirectCallback();
      window.history.replaceState({}, document.title, window.location.pathname);
    }

  }
  async login(): Promise<void> {
    await this.client!.loginWithRedirect();
  }

  async logout(): Promise<void> {
    await this.client!.logout({ logoutParams: { returnTo: window.location.origin } });
  }

  async getAccessToken(): Promise<string | undefined> {
    return this.client!.getTokenSilently().catch(() => undefined);
  }

  async isAuthenticated(): Promise<boolean> {
    return this.client!.isAuthenticated();
  }

  async getUser() {
    return this.client!.getUser();
  }
}
