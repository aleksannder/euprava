import { Component } from '@angular/core';
import {ActivatedRoute, Router, RouterOutlet} from '@angular/router';
import {DashboardComponent} from './components/ui/dashboard/dashboard.component';
import {FooterComponent} from './components/ui/footer/footer.component';
import {NgIf} from '@angular/common';
import {DashboardPageComponent} from './components/pages/dashboard-page/dashboard-page.component';
import {AuthService} from '@auth0/auth0-angular';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, DashboardComponent, FooterComponent, NgIf, DashboardPageComponent],
  template: `
    <app-dashboard-page *ngIf="!isAuthPage"></app-dashboard-page>
    <router-outlet *ngIf="isAuthPage"></router-outlet>
  `,
  styleUrl: './app.component.scss'
})
export class AppComponent {

  constructor(private router: Router) {}

  get isAuthPage(): boolean {
    const url = this.router.url;
    return url.startsWith('/login') || url.startsWith('/register');
  }
}
