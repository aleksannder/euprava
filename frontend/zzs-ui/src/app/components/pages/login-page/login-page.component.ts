import { Component } from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatButton, MatIconButton} from '@angular/material/button';
import {NgIf} from '@angular/common';
import {AuthService} from '@auth0/auth0-angular';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [
    MatCard,
    MatCardHeader,
    MatIcon,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatError,
    MatLabel,
    MatCardTitle,
    MatIconButton,
    MatButton,
    MatCardContent,
    NgIf,
    RouterLink
  ],
  templateUrl: `./login-page.component.html`,
  styleUrl: './login-page.component.scss',
  providers: [AuthService]
})
export class LoginPageComponent {
    loginForm: FormGroup;

    constructor(public auth: AuthService, private fb: FormBuilder) {
      this.loginForm = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
      })
    }

    onSubmit(): void {
      if (this.loginForm.invalid) {
        return;
      }
      const email = (this.loginForm.value.email as string)?.trim();

      try {
        this.auth.loginWithRedirect({
          authorizationParams: email ? {login_hint: email} : {},
        });
      } catch (error) {
        console.error(error);
      }
    }

}
