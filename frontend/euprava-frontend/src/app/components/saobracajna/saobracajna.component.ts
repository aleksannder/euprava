import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { SaobracajnaDozvola, SaobracajnaService } from '../../services/saobracajna.service';
import { NgForm } from '@angular/forms';
import {Role} from "../../models/korisnik";
import {RoleService} from "../../services/role.service";

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
  token: string = '';
  rola: Role = Role.EMPLOYER;
  trenutnaGodina: number = new Date().getFullYear();

  noviZahtev: NoviZahtev = {
    marka: '',
    model: '',
    kubikaza: 0,
    godiste: 0,
    vrstaPogona: '',
    tablice: ''
  };
  modalInstance: any;

  showAlert: boolean = false;
  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';

  constructor(private saobracajnaService: SaobracajnaService, private roleService: RoleService) { }

  ngOnInit(): void {
    this.token = localStorage.getItem('jwtToken') || '';
    this.roleService.getRoles$().subscribe(roles => {
      const role = roles[0];

      switch (role) {
        case 'CITIZEN':
          this.rola = Role.CITIZEN;
          break;
        case 'EMPLOYER':
          this.rola = Role.EMPLOYER;
          break;
        default:
          this.rola = Role.CITIZEN;
      }
    });

    this.ucitajSaobracajne();
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

    this.saobracajnaService.podnesiZahtev(this.noviZahtev).subscribe({
      next: res => {
        if (this.modalInstance) this.modalInstance.hide();
        form.resetForm();
        this.ucitajSaobracajne();

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

  ucitajSaobracajne(): void {
    this.saobracajnaService.dohvatiSve().subscribe({
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

  get isEmployer(): boolean {
    return this.rola === Role.EMPLOYER;
  }

  prihvatiZahtev(id: number) {
    this.saobracajnaService.odobriZahtev(id).subscribe({
      next: res => {
        this.ucitajSaobracajne();
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
        this.ucitajSaobracajne();
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
    this.saobracajnaService.produzi().subscribe({
      next: (res: any) => {
        this.ucitajSaobracajne();
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
