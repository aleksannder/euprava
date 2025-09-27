import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import {AuthService} from "@auth0/auth0-angular";
import {RoleService} from "./services/role.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  currentYear: number = new Date().getFullYear();
  title = 'euprava-frontend';
  public imagePath = 'assets/report.png';
  public imagePath2 = 'assets/accoun.png';
  public showProfileMenu: boolean = false;

  constructor(private authService: AuthService, private router: Router, private roleService: RoleService) {}

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

  logout() {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['/login']);
    });
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

  get isEmployer(): boolean {
    return this.roleService.hasRole('EMPLOYER');
  }

}
