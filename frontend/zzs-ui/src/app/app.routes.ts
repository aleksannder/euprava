import { Routes } from '@angular/router';
import {LoginPageComponent} from './components/pages/login-page/login-page.component';
import {DashboardPageComponent} from './components/pages/dashboard-page/dashboard-page.component';
import {RegisterPageComponent} from './components/pages/register-page/register-page.component';

export const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'dashboard', component: DashboardPageComponent },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
];
