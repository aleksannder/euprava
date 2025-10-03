import {Component, HostListener, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import {AuthService} from "@auth0/auth0-angular";
import {Observable} from "rxjs";
import {AuthTokenUtil} from "./interceptor/auth-token.util";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  currentYear: number = new Date().getFullYear();
  isEmployer$!: Observable<boolean>;
  title = 'euprava-frontend';
  public imagePath = 'assets/report.png';
  public imagePath2 = 'assets/accoun.png';
  public showProfileMenu: boolean = false;

  constructor(private authService: AuthService, private router: Router, private authUtil: AuthTokenUtil) {
    this.isEmployer$ = this.authUtil.hasPermission('mup:employer');
  }

  ngOnInit(): void {
    this.authService.getAccessTokenSilently().subscribe({
      next: (token) => {
        console.log('Silent login uspešan, token:', token);
      },
      error: (err) => {
        console.warn('Silent login nije uspeo, redirect na login', err);
        // this.authService.loginWithRedirect();
      }
    });
  }

  get isLoggedIn$() {
    return this.authService.isAuthenticated$;
  }

  toggleProfileMenu() {
    this.showProfileMenu = !this.showProfileMenu;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    const clickedInsideMenu = target.closest('.profile-container') || target.closest('.profile-menu');
    if (!clickedInsideMenu) {
      this.showProfileMenu = false;
    }
  }

  logout(): void {
    this.authService.logout({ logoutParams: {
        returnTo: window.location.origin
      }});
  }

  goToProfile() {
    this.showProfileMenu = false;
    this.router.navigate(['/profile']);
  }

  goToLicna() {
    this.showProfileMenu = false;
    this.router.navigate(['/licna']);
  }

  goToDashboard() {
    this.showProfileMenu = false;
    this.router.navigate(['/dashboard']);
  }

  goToVozacka() {
    this.showProfileMenu = false;
    this.router.navigate(['/vozacka']);
  }

  goToSaobracajna() {
    this.showProfileMenu = false;
    this.router.navigate(['/saobracajna']);
  }

  goToOruzje() {
    this.showProfileMenu = false;
    this.router.navigate(['/oruzje']);
  }

  goToOstalo() {
    this.showProfileMenu = false;
    this.router.navigate(['/ostalo']);
  }

  goToSurveys(): void {
    this.showProfileMenu = false;
    this.router.navigate(['/surveys']);
  }

}
