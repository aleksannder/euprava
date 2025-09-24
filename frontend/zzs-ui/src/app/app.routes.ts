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

export const routes: Routes = [
    // Login
  {
    path: 'login', canActivate: [], component: LoginPageComponent
  },
    // Register
  {
    path: 'register', canActivate: [], component: RegisterPageComponent
  },
    // Citizen routes
  {
    path: 'dashboard', canActivate: [], component: DashboardPageComponent
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
    path: '**', redirectTo: 'not-found'
  },
];
