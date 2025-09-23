import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatIcon} from '@angular/material/icon';
import {MatInput} from '@angular/material/input';
import {NgIf} from '@angular/common';
import {MatSnackBar} from '@angular/material/snack-bar';
import {UsersService} from '../../../services/users/users.service';
import {AuthService} from '@auth0/auth0-angular';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [
    FormsModule,
    MatButton,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    MatError,
    MatFormField,
    MatIcon,
    MatIconButton,
    MatInput,
    MatLabel,
    NgIf,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.scss',
  providers: [UsersService, AuthService]
})
export class RegisterPageComponent {
  registerForm: FormGroup;
  hide = true;
  constructor(private fb: FormBuilder,
              private usersService: UsersService,
              private snackbar: MatSnackBar,
              private auth0: AuthService,
  ) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  onSubmit() {
    if (!this.registerForm.valid) {
      return;
    }
    this.usersService.register(this.registerForm.value).subscribe({
      next: () => {
        this.snackbar.open("Registracija uspesna. Molimo vas da se ulogujete.", undefined, {duration: 3000});
        this.auth0.loginWithRedirect();
      },
      error: () => this.snackbar.open("Doslo je do greske", undefined, { duration: 3000 })
    })
  }
}
