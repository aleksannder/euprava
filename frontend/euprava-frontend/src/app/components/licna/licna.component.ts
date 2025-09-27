import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { LicnaKarta } from "../../models/LicnaKarta";
import { ZahteviService } from "../../services/zahtevi.service";
import {Role} from "../../models/korisnik";
import {RoleService} from "../../services/role.service";

declare var bootstrap: any;

@Component({
  selector: 'app-licna',
  templateUrl: './licna.component.html',
  styleUrls: ['./licna.component.css']
})
export class LicnaComponent implements OnInit, AfterViewInit {
  @ViewChild('zahtevModal', { static: false }) zahtevModalRef!: ElementRef;

  zahtevi: LicnaKarta[] = [];
  token: string = '';
  rola: Role = Role.EMPLOYER;

  noviZahtev = {
    jmbg: '',
    datumRodjenja: '',
    pol: 'M',
    grad: ''
  };

  modalInstance: any;
  today: string = '';

  showAlert: boolean = false;
  alertMessage: string = '';
  alertType: 'success' | 'error' = 'error';

  constructor(private zahteviService: ZahteviService, private roleService: RoleService) { }

  ngOnInit(): void {
    this.token = localStorage.getItem('jwtToken') || '';
    //todo
    this.roleService.getRoles$().subscribe(roles => {
      const role = roles[0];

      switch (role) {
        case 'EMPLOYER':
          this.rola = Role.EMPLOYER;
          break;
        case 'CITIZEN':
          this.rola = Role.CITIZEN;
          break;
        default:
          this.rola = Role.CITIZEN;
      }
    });

    this.ucitajZahteve();

    const danas = new Date();
    danas.setDate(danas.getDate() - 1);
    this.today = danas.toISOString().split('T')[0];
  }

  ngAfterViewInit(): void {
    if (this.zahtevModalRef && this.zahtevModalRef.nativeElement) {
      this.modalInstance = new bootstrap.Modal(this.zahtevModalRef.nativeElement);
    }
  }

  openZahtevModal(): void {
    if (this.modalInstance) {
      this.modalInstance.show();
    } else {
      console.error('Modal nije inicijalizovan!');
    }
  }

  podnesiZahtev(): void {
    this.zahteviService.podnesiZahtev(this.token, this.noviZahtev).subscribe({
      next: (res) => {
        this.ucitajZahteve();
        this.alertType = 'success';
        this.alertMessage = res || 'Zahtev za izdavanje lične karte je uspešno podnet!';
        this.showAlert = true;
      },
      error: (err) => {
        this.alertType = 'error';
        this.alertMessage = err.error || 'Došlo je do greške pri podnošenju zahteva.';
        this.showAlert = true;
      }
    });
  }

  ucitajZahteve(): void {
    this.zahteviService.getSviZahtevi(this.token).subscribe({
      next: (data) => this.zahtevi = data,
      error: (err) => console.error(err)
    });
  }

  closeAlert(): void {
    this.showAlert = false;
  }

  get imaAktivanZahtev(): boolean {
    return this.zahtevi.some(zahtev => zahtev.status === 'CEKANJE' || zahtev.status === 'DOZVOLJEN');
  }

  get isEmployer(): boolean {
    return this.rola === Role.EMPLOYER;
  }

  prihvatiZahtev(id: number) {
    this.zahteviService.prihvatiZahtev(this.token, id).subscribe({
      next: (res: any) => {
        this.ucitajZahteve();

        this.alertMessage = res?.message || 'Zahtev je odobren!';
        this.alertType = 'success';
        this.showAlert = true;
      },
      error: err => {
        console.error(err);
        this.alertMessage = err.error?.message || 'Došlo je do greške pri prihvatanju zahteva.';
        this.alertType = 'error';
        this.showAlert = true;
      }
    });
  }

  odbijZahtev(id: number) {
    this.zahteviService.odbijZahtev(this.token, id).subscribe({
      next: (res: any) => {
        this.ucitajZahteve();
        this.alertMessage = res?.message || 'Zahtev je odbijen!';
        this.alertType = 'success';
        this.showAlert = true;
      },
      error: err => {
        console.error(err);
        this.alertMessage = err.error?.message || 'Došlo je do greške pri odbijanju zahteva.';
        this.alertType = 'error';
        this.showAlert = true;
      }
    });
  }

  get mozeProduziti(): boolean {
    return this.zahtevi.some(zahtev => zahtev.status === 'DOZVOLJEN');
  }

  produziLicnuKartu() {
    this.zahteviService.produziLicnu(this.token).subscribe({
      next: (res: string) => {
        this.alertType = 'success';
        this.alertMessage = res || 'Lična karta je produžena!';
        this.showAlert = true;
        this.ucitajZahteve();
      },
      error: err => {
        this.alertType = 'error';
        this.alertMessage = err.error || 'Došlo je do greške pri produženju.';
        this.showAlert = true;
      }
    });
  }

}
