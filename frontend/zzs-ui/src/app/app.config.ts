import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {authInterceptor} from './services/auth/auth.interceptor';
import {provideAuth0} from '@auth0/auth0-angular';

// ZZS-APP SPA
const AUTH0_DOMAIN: string = 'dev-7wsgpcp2kp2ul4ct.us.auth0.com';
const AUTH0_CLIENT_ID: string = 'pHoKHd84VSWN5sa8F5ymTjIMcss7NneH';

// ZZS-API
const AUTH0_IDENTIFIER: string = 'https://api.zzs';
export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideAuth0({
      domain: AUTH0_DOMAIN,
      clientId: AUTH0_CLIENT_ID,
      authorizationParams: {
        redirect_uri: window.location.origin,
        audience: AUTH0_IDENTIFIER,
        scope: 'openid profile email'
      },
      cacheLocation: 'localstorage',
      useRefreshTokens: true
    }),
  ],
};
