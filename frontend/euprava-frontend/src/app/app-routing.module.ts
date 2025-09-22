import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from "./components/login/login.component";
import { RegisterComponent } from "./components/register/register.component";
import { CustomAlertComponent } from "./components/custom-alert/custom-alert.component";
import { DashboardComponent } from "./components/dashboard/dashboard.component";
import {AuthGuard} from "./guards/AuthGuard";
import {LicnaComponent} from "./components/licna/licna.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {VozackaComponent} from "./components/vozacka/vozacka.component";
import {SaobracajnaComponent} from "./components/saobracajna/saobracajna.component";
import {OruzjeComponent} from "./components/oruzje/oruzje.component";
import {OstaloComponent} from "./components/ostalo/ostalo.component";

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'custom-alert', component: CustomAlertComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'licna', component: LicnaComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'vozacka', component: VozackaComponent, canActivate: [AuthGuard] },
  { path: 'saobracajna', component: SaobracajnaComponent, canActivate: [AuthGuard] },
  { path: 'oruzje', component: OruzjeComponent, canActivate: [AuthGuard] },
  { path: 'ostalo', component: OstaloComponent, canActivate: [AuthGuard] },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
