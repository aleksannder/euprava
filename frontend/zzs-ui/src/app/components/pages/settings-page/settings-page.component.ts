import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Region} from '../../../model/enums/region.enum';
import {UsersService} from '../../../services/users/users.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {User} from '@auth0/auth0-spa-js';
import {AuthService} from '@auth0/auth0-angular';
import {map, take} from 'rxjs';
import {UserInfo} from '../../../model/register-user.model';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatOption, MatSelect} from '@angular/material/select';
import {RegionUtil} from '../../../services/util/region.util';
import {MatButton} from '@angular/material/button';
import {MatInput} from '@angular/material/input';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-settings-page',
  standalone: true,
  imports: [
    MatFormField,
    MatSelect,
    MatLabel,
    MatOption,
    ReactiveFormsModule,
    MatButton,
    MatInput,
    NgForOf
  ],
  templateUrl: './settings-page.component.html',
  styleUrl: './settings-page.component.scss',
  providers: [UsersService]
})
export class SettingsPageComponent implements OnInit {
    form!: FormGroup;
    regions: Region[] = Object.values(Region);
    userEmail!: string;
    user!: UserInfo;

    constructor(
      private fb: FormBuilder,
      private usersService: UsersService,
      private snackbar: MatSnackBar,
      private auth: AuthService,
    ) {
      this.auth.user$.pipe(
        take(1)).subscribe(user => {
         this.userEmail = user?.email as string;
        })
    }

    ngOnInit(): void {
      this.form = this.fb.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        city: ['', Validators.required],
        address: ['', Validators.required],
        gender: ['', Validators.required],
        region: ['', Validators.required],

        jmbg: [{ value: '', disabled: true }],
        email: [{ value: '', disabled: true }],
      });

      // kada se podaci učitaju, patch-ujemo formu
      this.usersService.getUserInfo(this.userEmail).subscribe(userData => {
        this.user = userData;
        this.form.patchValue({
          firstName: this.user.firstName,
          lastName: this.user.lastName,
          city: this.user.city,
          address: this.user.address,
          gender: this.user.gender,
          region: this.user.region,
          jmbg: this.user.jmbg,
          email: this.user.email,
        });
      });
    }

    onSubmit() {
      if (this.form.invalid) {
        return;
      }
      const updated: UserInfo = {...this.form.getRawValue()};
      this.usersService.updateUserInfo(updated, updated.email).subscribe({
        next: () => {
          this.snackbar.open('Uspešno ažurirano', undefined, {duration: 2500});
        },
        error: () => {
          this.snackbar.open('Došlo je do greške', undefined, {duration: 2500});
        }
      });
    }

  protected readonly RegionUtil = RegionUtil;
}
