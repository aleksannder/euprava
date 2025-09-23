import { Component } from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {DashboardComponent} from './components/ui/dashboard/dashboard.component';
import {FooterComponent} from './components/ui/footer/footer.component';
import {NgIf} from '@angular/common';
import {filter} from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, DashboardComponent, FooterComponent, NgIf],
  template: `
    <app-dashboard *ngIf="!isAuthPage"></app-dashboard>
    <router-outlet></router-outlet>
  `,
  styleUrl: './app.component.scss'
})
export class AppComponent {
  isAuthPage = false;

  constructor(private router: Router) {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((e: NavigationEnd) => {
        const url = e.urlAfterRedirects.toLowerCase();
        const authPages = ['/login', '/register', '/unauthorized', '/not-found'];
        this.isAuthPage = authPages.some(path => url.startsWith(path));
      });
  }
}
