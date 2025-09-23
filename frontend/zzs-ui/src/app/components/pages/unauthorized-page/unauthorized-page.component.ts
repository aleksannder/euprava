import { Component, inject } from '@angular/core';
import {Router} from '@angular/router';
import {MatCard, MatCardContent} from '@angular/material/card';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-unauthorized-page',
  standalone: true,
  imports: [
    MatCardContent,
    MatCard,
    MatButton
  ],
  templateUrl: './unauthorized-page.component.html',
  styleUrl: './unauthorized-page.component.scss'
})
export class UnauthorizedPageComponent {
  private router = inject(Router);

  goToLogin() {
    this.router.navigate(['login']);
  }

  goToRegister() {
    this.router.navigate(['register']);
  }
}
