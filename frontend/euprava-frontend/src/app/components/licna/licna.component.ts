import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { LicnaKarta } from "../../models/LicnaKarta";
import { ZahteviService } from "../../services/zahtevi.service";
import {Role} from "../../models/korisnik";
import {AuthTokenUtil} from "../../interceptor/auth-token.util";
import {Observable} from "rxjs";

declare var bootstrap: any;

@Component({
  selector: 'app-licna',
  templateUrl: './licna.component.html',
  styleUrls: ['./licna.component.css']
})
export class LicnaComponent implements OnInit, AfterViewInit {
  @ViewChild('zahtevModal', { static: false }) zahtevModalRef!: ElementRef;
  isEmployer$!: Observable<boolean>;
  userEmail!: string;
  zahtevi: LicnaKarta[] = [];

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

  constructor(private zahteviService: ZahteviService, private authServiceUtil: AuthTokenUtil) {
    this.isEmployer$ = authServiceUtil.hasPermission('mup:employer');
  }

  ngOnInit(): void {

    this.authServiceUtil.getUserProfile().subscribe((user) => {
      this.userEmail = user.email;
      this.ucitajZahteve();
    })
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
    this.zahteviService.podnesiZahtev(this.userEmail, this.noviZahtev).subscribe({
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
    this.zahteviService.getSviZahtevi(this.userEmail).subscribe({
      next: (data) => {
        console.info(data);
        this.zahtevi = data
      },
      error: (err) => console.error(err)
    });
  }

  closeAlert(): void {
    this.showAlert = false;
  }

  get imaAktivanZahtev(): boolean {
    return this.zahtevi.some(zahtev => zahtev.status === 'CEKANJE' || zahtev.status === 'DOZVOLJEN');
  }

  prihvatiZahtev(id: number) {
    this.zahteviService.prihvatiZahtev(id).subscribe({
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
    this.zahteviService.odbijZahtev(id).subscribe({
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
    this.zahteviService.produziLicnu(this.userEmail).subscribe({
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
