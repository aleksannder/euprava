import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule, HTTP_INTERCEPTORS, provideHttpClient, withInterceptors} from "@angular/common/http";
import { CustomAlertComponent } from './components/custom-alert/custom-alert.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import {authInterceptor} from "./interceptor/AuthInterceptor";
import { LicnaComponent } from './components/licna/licna.component';
import { ProfileComponent } from './components/profile/profile.component';
import { VozackaComponent } from './components/vozacka/vozacka.component';
import { SaobracajnaComponent } from './components/saobracajna/saobracajna.component';
import {CommonModule} from "@angular/common";
import { OruzjeComponent } from './components/oruzje/oruzje.component';
import { OstaloComponent } from './components/ostalo/ostalo.component';
import {AuthModule} from "@auth0/auth0-angular";
import {SurveysComponent} from "./components/surveys/surveys.component";

const AUTH0_DOMAIN: string = 'dev-7wsgpcp2kp2ul4ct.us.auth0.com';
const AUTH0_CLIENT_ID: string = 'RXUhNLnPVrqyzaMtQc5OgAVOg8Gu35WI';

const AUTH0_IDENTIFIER: string = 'https://api.shared';
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
    SurveysComponent,
    OstaloComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    CommonModule,
    AuthModule.forRoot({
      domain: AUTH0_DOMAIN,
      clientId: AUTH0_CLIENT_ID,
      authorizationParams: {
        redirect_uri: window.location.origin,
        audience: AUTH0_IDENTIFIER,
        scope: 'openid profile email offline_access'
      },
      useRefreshTokens: true,
      cacheLocation: 'localstorage'
    }),
  ],
  providers: [provideHttpClient(withInterceptors([authInterceptor]))],
  bootstrap: [AppComponent]
})
export class AppModule { }
