import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { CustomAlertComponent } from './components/custom-alert/custom-alert.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import {AuthInterceptor} from "./interceptor/AuthInterceptor";
import { LicnaComponent } from './components/licna/licna.component';
import { ProfileComponent } from './components/profile/profile.component';
import { VozackaComponent } from './components/vozacka/vozacka.component';
import { SaobracajnaComponent } from './components/saobracajna/saobracajna.component';
import {CommonModule} from "@angular/common";
import { OruzjeComponent } from './components/oruzje/oruzje.component';
import { OstaloComponent } from './components/ostalo/ostalo.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    CustomAlertComponent,
    DashboardComponent,
    LicnaComponent,
    ProfileComponent,
    VozackaComponent,
    SaobracajnaComponent,
    OruzjeComponent,
    OstaloComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    CommonModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
