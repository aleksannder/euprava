import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {AuthService} from "@auth0/auth0-angular";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  public imagePath = 'assets/administration.jpg';
  public loginForm: FormGroup;
  public alertMessage: string = '';
  public alertType: 'success' | 'error' = 'error';
  public showModal: boolean = false;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  public goToRegister(): void {
    this.router.navigate(['/register']);
  }

  public handleModalClose() {
    this.showModal = false;
    if (this.alertType === 'success') {
      this.router.navigate(['/dashboard']);
    }
  }

  public onSubmit(): void {
    if (this.loginForm.valid) {
      try {
        this.authService.loginWithRedirect({
          authorizationParams: { login_hint: this.loginForm.controls['email'].value }
        }).subscribe({
          next: () => {
            this.router.navigate(['/dashboard']);
          },
          error: (error) => {
            this.alertMessage = 'Login failed: ' + (error?.message || 'Unknown error');
          }
        });
      } catch (exception) {
        this.alertMessage = 'Unexpected error occurred';
      }
    } else {
      this.alertMessage = 'Form is invalid. Please check your inputs.';
    }
  }

}
