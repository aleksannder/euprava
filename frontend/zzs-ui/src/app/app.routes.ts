import { Routes } from '@angular/router';
import {LoginPageComponent} from './components/pages/login-page/login-page.component';
import {RegisterPageComponent} from './components/pages/register-page/register-page.component';
import {
  UnauthorizedPageComponent
} from './components/pages/unauthorized-page/unauthorized-page.component';
import {NotFoundPageComponent} from './components/pages/not-found-page/not-found-page.component';
import {HomeComponent} from './components/pages/home/home.component';
import {PopulationPageComponent} from './components/pages/population-page/population-page.component';
import {GdpPageComponent} from './components/pages/gdp-page/gdp-page.component';
import {WagePageComponent} from './components/pages/wage-page/wage-page.component';
import {TrafficPageComponent} from './components/pages/traffic-page/traffic-page.component';
import {SurveysPageComponent} from './components/pages/surveys-page/surveys-page.component';
import {SurveyFillPageComponent} from './components/pages/survey-fill-page/survey-fill-page.component';
import {analystGuard, citizenGuard, authGuard} from './services/auth/authorization.guard';
import {SettingsPageComponent} from './components/pages/settings-page/settings-page.component';

export const routes: Routes = [
  {
    path: 'login', component: LoginPageComponent
  },
  {
    path: 'register', component: RegisterPageComponent
  },
  {
    path: 'home', component: HomeComponent, canActivate: [authGuard]
  },
  {
    path: 'settings', component: SettingsPageComponent, canActivate: [authGuard],
  },
  {
    path: 'population', canActivate: [authGuard], component: PopulationPageComponent
  },
  {
    path: 'gdp', canActivate: [authGuard], component: GdpPageComponent
  },
  {
    path: 'wage', canActivate: [authGuard], component: WagePageComponent
  },
  {
    path: 'traffic', canActivate: [authGuard], component: TrafficPageComponent
  },
  {
    path: 'surveys', canActivate: [authGuard], component: SurveysPageComponent
  },
  {
    path: 'surveys/:id/fill', canActivate: [authGuard], component: SurveyFillPageComponent
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
