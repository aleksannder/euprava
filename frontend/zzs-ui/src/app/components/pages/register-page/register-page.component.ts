import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatError, MatFormField, MatFormFieldModule, MatLabel} from '@angular/material/form-field';
import {MatIcon} from '@angular/material/icon';
import {MatInput, MatInputModule} from '@angular/material/input';
import {NgForOf, NgIf} from '@angular/common';
import {MatSnackBar} from '@angular/material/snack-bar';
import {UsersService} from '../../../services/users/users.service';
import {AuthService} from '@auth0/auth0-angular';
import {RouterLink} from '@angular/router';
import {Region} from '../../../model/enums/region.enum';
import {RegisterUserRequest} from '../../../model/register-user.model';
import {MatNativeDateModule, MatOption, provideNativeDateAdapter} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {RegionUtil} from '../../../services/util/region.util';
import {
  MatDatepicker,
  MatDatepickerInput,
  MatDatepickerModule,
  MatDatepickerToggle
} from '@angular/material/datepicker';

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
    MatDatepicker,
    MatInput,
    MatLabel,
    NgIf,
    ReactiveFormsModule,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatOption,
    NgForOf,
    MatSelect,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatNativeDateModule,
  ],
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.scss',
  providers: [UsersService, AuthService, provideNativeDateAdapter()],
})
export class RegisterPageComponent {
  registerForm: FormGroup;
  regions: Region[] = Object.values(Region);
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
      region: [Region.RS11_BELGRADE, [Validators.required]],
      city: ['', Validators.required],
      address: ['', Validators.required],
      gender: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
    });
  }

  onSubmit() {
    if (!this.registerForm.valid) {
      return;
    }
    const request: RegisterUserRequest = this.registerForm.value;
    this.usersService.register(request).subscribe({
      next: () => {
        this.snackbar.open("Registracija uspesna. Molimo vas da se ulogujete.", undefined, {duration: 3000});
        this.auth0.loginWithRedirect();
      },
      error: () => this.snackbar.open("Doslo je do greske", undefined, { duration: 3000 })
    })
  }

  protected readonly RegionUtil = RegionUtil;
  protected readonly Region = Region;
}
