import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { SaobracajnaDozvola, SaobracajnaService } from '../../services/saobracajna.service';
import { NgForm } from '@angular/forms';
import {Role} from "../../models/korisnik";
import {RoleService} from "../../services/role.service";
import {Observable} from "rxjs";
import {AuthTokenUtil} from "../../interceptor/auth-token.util";

declare var bootstrap: any;
interface NoviZahtev {
  marka: string;
  model: string;
  kubikaza: number;
  godiste: number;
  vrstaPogona: string;
  tablice: string;
}
@Component({
  selector: 'app-saobracajna',
  templateUrl: './saobracajna.component.html',
  styleUrls: ['./saobracajna.component.css']
})
export class SaobracajnaComponent implements OnInit, AfterViewInit {

  @ViewChild('saobracajnaModal', { static: false }) saobracajnaModalRef!: ElementRef;

  saobracajne: SaobracajnaDozvola[] = [];
  trenutnaGodina: number = new Date().getFullYear();
  isEmployer$!: Observable<boolean>;
  noviZahtev: NoviZahtev = {
    marka: '',
    model: '',
    kubikaza: 0,
    godiste: 0,
    vrstaPogona: '',
    tablice: ''
  };
  modalInstance: any;
  userEmail!: string;
  showAlert: boolean = false;
  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';

  constructor(private saobracajnaService: SaobracajnaService, private authUtil: AuthTokenUtil) {
    this.isEmployer$ = this.authUtil.hasPermission('mup:employer');
  }

  ngOnInit(): void {
    this.authUtil.getUserProfile().subscribe((user) => {
      this.userEmail = user.email;
      this.ucitajSaobracajne(this.userEmail);
    })
  }

  ngAfterViewInit(): void {
    if (this.saobracajnaModalRef && this.saobracajnaModalRef.nativeElement) {
      this.modalInstance = new bootstrap.Modal(this.saobracajnaModalRef.nativeElement);
    }
  }

  openSaobracajnaModal(): void {
    if (this.modalInstance) this.modalInstance.show();
  }

  podnesiZahtev(form: NgForm): void {
    if (form.invalid) return;

    this.saobracajnaService.podnesiZahtev(this.noviZahtev, this.userEmail).subscribe({
      next: res => {
        if (this.modalInstance) this.modalInstance.hide();
        form.resetForm();
        this.ucitajSaobracajne(this.userEmail);

        this.alertType = 'success';
        this.alertMessage = res || 'Zahtev uspešno podnet!';
        this.showAlert = true;
      },
      error: err => {
        console.error(err);
        this.alertType = 'error';
        this.alertMessage = err?.error || 'Greška pri podnošenju zahteva.';
        this.showAlert = true;
      }
    });

  }

  ucitajSaobracajne(email: string): void {
    this.saobracajnaService.dohvatiSve(email).subscribe({
      next: (res) => this.saobracajne = res,
      error: (err) => console.error(err)
    });
  }

  closeAlert(): void {
    this.showAlert = false;
  }

  get imaAktivanZahtev(): boolean {
    return this.saobracajne.some(z => z.status === 'CEKANJE' || z.status === 'DOZVOLJEN');
  }

  prihvatiZahtev(id: number) {
    this.saobracajnaService.odobriZahtev(id).subscribe({
      next: res => {
        this.ucitajSaobracajne(this.userEmail);
        this.alertMessage = res?.message || 'Zahtev odobren!';
        this.alertType = 'success';
        this.showAlert = true;
      },
      error: err => {
        this.alertMessage = err.error?.message || 'Greška pri odobravanju.';
        this.alertType = 'error';
        this.showAlert = true;
      }
    });
  }

  odbijZahtev(id: number) {
    this.saobracajnaService.odbijZahtev(id).subscribe({
      next: res => {
        this.ucitajSaobracajne(this.userEmail);
        this.alertMessage = res?.message || 'Zahtev odbijen!';
        this.alertType = 'success';
        this.showAlert = true;
      },
      error: err => {
        this.alertMessage = err.error?.message || 'Greška pri odbijanju.';
        this.alertType = 'error';
        this.showAlert = true;
      }
    });
  }

  produziSaobracajnu() {
    this.saobracajnaService.produzi(this.userEmail).subscribe({
      next: (res: any) => {
        this.ucitajSaobracajne(this.userEmail);
        this.alertType = 'success';
        this.alertMessage = res?.message || 'Saobraćajna je produžena!';
        this.showAlert = true;
      },
      error: err => {
        this.alertType = 'error';
        this.alertMessage = err.error?.message || 'Dozvola još nije spremna za produženje (više od mesec dana do isteka).';
        this.showAlert = true;
      }
    });
  }

  get mozeProduziti(): boolean {
    return this.saobracajne.some(z => z.status === 'DOZVOLJEN');
  }
}
