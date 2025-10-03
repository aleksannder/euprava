import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from "./components/login/login.component";
import { RegisterComponent } from "./components/register/register.component";
import { CustomAlertComponent } from "./components/custom-alert/custom-alert.component";
import { DashboardComponent } from "./components/dashboard/dashboard.component";
import {LicnaComponent} from "./components/licna/licna.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {VozackaComponent} from "./components/vozacka/vozacka.component";
import {SaobracajnaComponent} from "./components/saobracajna/saobracajna.component";
import {OruzjeComponent} from "./components/oruzje/oruzje.component";
import {OstaloComponent} from "./components/ostalo/ostalo.component";
import {authGuard, citizenGuard} from "./guards/AuthGuard";
import {SurveysComponent} from "./components/surveys/surveys.component";

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'custom-alert', component: CustomAlertComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'licna', component: LicnaComponent, canActivate: [authGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  { path: 'vozacka', component: VozackaComponent, canActivate: [authGuard] },
  { path: 'saobracajna', component: SaobracajnaComponent, canActivate: [authGuard] },
  { path: 'oruzje', component: OruzjeComponent, canActivate: [authGuard]},
  { path: 'ostalo', component: OstaloComponent, canActivate: [authGuard]},
  { path: 'surveys', component: SurveysComponent, canActivate: [authGuard, citizenGuard]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
