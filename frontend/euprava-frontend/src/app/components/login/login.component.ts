import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

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
      lozinka: ['', Validators.required]
    });
  }

  public goToRegister(): void {
    this.router.navigate(['/register']);
  }

  private showAlert(message: string, type: 'success' | 'error' = 'error') {
    this.alertMessage = message;
    this.alertType = type;
    this.showModal = true;
  }

  public handleModalClose() {
    this.showModal = false;
    if (this.alertType === 'success') {
      this.router.navigate(['/dashboard']);
    }
  }

  public onSubmit(): void {
    if (this.loginForm.valid) {
      const { email, lozinka } = this.loginForm.value;

      this.authService.login(email, lozinka).subscribe({
        next: (response: any) => {
          localStorage.setItem('jwtToken', response.token);

          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          this.showAlert(err.error || 'Inkredencijali nisu tačni', 'error');
        }
      });

    } else {
      this.showAlert('Molimo popunite sva polja ispravno.', 'error');
    }
  }

}
