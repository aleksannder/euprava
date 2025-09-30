import { Routes } from '@angular/router';
import {LoginPageComponent} from './components/pages/login-page/login-page.component';
import {DashboardPageComponent} from './components/pages/dashboard-page/dashboard-page.component';
import {RegisterPageComponent} from './components/pages/register-page/register-page.component';
import {authorizationGuard} from './services/auth/authorization.guard';
import {roleGuard} from './services/auth/role.guard';
import {Role} from './model/register-user.model';
import {
  UnauthorizedPageComponent
} from './components/pages/unauthorized-page/unauthorized-page.component';
import {NotFoundPageComponent} from './components/pages/not-found-page/not-found-page.component';
import {DomainsComponent} from './components/pages/domains/domains.component';
import {HomeComponent} from './components/pages/home/home.component';
import {SubdomainComponent} from './components/pages/subdomain/subdomain.component';
import {PopulationPageComponent} from './components/pages/population-page/population-page.component';
import {GdpPageComponent} from './components/pages/gdp-page/gdp-page.component';
import {WagePageComponent} from './components/pages/wage-page/wage-page.component';
import {TrafficPageComponent} from './components/pages/traffic-page/traffic-page.component';

export const routes: Routes = [
    // Login
  {
    path: 'login', canActivate: [], component: LoginPageComponent
  },
    // Register
  {
    path: 'register', canActivate: [], component: RegisterPageComponent
  },
  {
    path: 'home', canActivate: [], component: HomeComponent
  },
  {
    path: 'population', canActivate: [], component: PopulationPageComponent
  },
  {
    path: 'gdp', canActivate: [], component: GdpPageComponent
  },
  {
    path: 'wage', canActivate: [], component: WagePageComponent
  },
  {
    path: 'traffic', canActivate: [], component: TrafficPageComponent
  },


  // Unauthorized page
  {
    path: 'unauthorized', component: UnauthorizedPageComponent
  },

  {
    path: 'not-found', component: NotFoundPageComponent
  },

  // Wildcard
  {
    path: '**', redirectTo: 'home'
  },
];
