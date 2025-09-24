import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import {Korisnik} from "../../models/korisnik";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  public imagePath: string = 'assets/administar.jpg';
  public registerForm: FormGroup;
  public alertMessage: string = '';
  public alertType: 'success' | 'error' = 'error';
  public showModal: boolean = false;
  public today: string = '';

  get ime() { return this.registerForm.get('ime'); }
  get prezime() { return this.registerForm.get('prezime'); }
  get email() { return this.registerForm.get('email'); }
  get lozinka() { return this.registerForm.get('lozinka'); }
  get datumRodjenja() { return this.registerForm.get('datumRodjenja'); }
  get grad() { return this.registerForm.get('grad'); }
  get adresa() { return this.registerForm.get('adresa'); }
  get pol() { return this.registerForm.get('pol'); }

  constructor(
    private fb: FormBuilder,
    private router: Router,
  ) {
    const danas = new Date();
    danas.setDate(danas.getDate() - 1);
    this.today = danas.toISOString().split('T')[0];

    this.registerForm = this.fb.group({
      ime: ['', Validators.required],
      prezime: ['', Validators.required],
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          Validators.pattern(/^[a-zA-Z0-9._%+-]+@gmail\.com$/)
        ]
      ],
      lozinka: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,}$/)
        ]
      ],
      rola: ['CITIZEN', Validators.required],
      datumRodjenja: ['', [Validators.required, this.pastDateValidator]],
      grad: ['', Validators.required],
      adresa: ['', Validators.required],
      pol: ['', Validators.required]
    });

  }

  private pastDateValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;
    const inputDate = new Date(value);
    const danas = new Date();
    if (inputDate >= danas) {
      return { futureDate: true };
    }
    return null;
  }

  public goToLogin(): void {
    this.router.navigate(['/login']);
  }

  private showAlert(message: string, type: 'success' | 'error' = 'error') {
    this.alertMessage = message;
    this.alertType = type;
    this.showModal = true;
  }

  public handleModalClose() {
    this.showModal = false;
    if (this.alertType === 'success') {
      this.router.navigate(['/login']);
    }
  }

  public onSubmit(): void {
    const imeCtrl = this.registerForm.get('ime');
    const prezimeCtrl = this.registerForm.get('prezime');
    const emailCtrl = this.registerForm.get('email');
    const lozinkaCtrl = this.registerForm.get('lozinka');
    const datumCtrl = this.registerForm.get('datumRodjenja');

    if (!imeCtrl?.value || !prezimeCtrl?.value || !emailCtrl?.value || !lozinkaCtrl?.value || !datumCtrl?.value) {
      this.showAlert('Molimo popunite sva polja.', 'error');
      return;
    }

    if (datumCtrl.invalid) {
      if (datumCtrl.errors?.['futureDate']) {
        this.showAlert('Datum rođenja mora biti u prošlosti.', 'error');
        return;
      }
    }

    if (emailCtrl.invalid) {
      if (emailCtrl.errors?.['email'] || emailCtrl.errors?.['pattern']) {
        this.showAlert('Email mora biti u formatu @gmail.com.', 'error');
        return;
      }
    }

    if (lozinkaCtrl.invalid) {
      if (lozinkaCtrl.errors?.['minlength']) {
        this.showAlert('Lozinka mora imati najmanje 8 karaktera.', 'error');
        return;
      }
      if (lozinkaCtrl.errors?.['pattern']) {
        this.showAlert('Lozinka mora sadržati veliko slovo, malo slovo i specijalan znak.', 'error');
        return;
      }
    }

    if (this.registerForm.valid) {
      // todo: handle register
      const korisnikData: Korisnik = this.registerForm.value;
      // this.authService.register(korisnikData).subscribe({
      //   next: () => this.showAlert('Registracija uspešna!', 'success'),
      //   error: (error) => this.showAlert(error.error || 'Došlo je do greške pri registraciji.', 'error')
      // });
    }
  }

}
