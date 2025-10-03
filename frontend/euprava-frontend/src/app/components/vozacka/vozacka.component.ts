import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { VozackaDozvola, VozackaService } from '../../services/vozacka.service';
import { NgForm } from '@angular/forms';
import {Role} from "../../models/korisnik";
import {RoleService} from "../../services/role.service";
import {AuthTokenUtil} from "../../interceptor/auth-token.util";
import {Observable} from "rxjs";

declare var bootstrap: any;

@Component({
  selector: 'app-vozacka',
  templateUrl: './vozacka.component.html',
  styleUrls: ['./vozacka.component.css']
})
export class VozackaComponent implements OnInit, AfterViewInit {

  @ViewChild('vozackaModal', { static: false }) vozackaModalRef!: ElementRef;

  vozacke: VozackaDozvola[] = [];
  userEmail!: string;
  noviZahtev = { grad: '', kategorijeStr: '' };
  modalInstance: any;
  isEmployer$!: Observable<boolean>;

  showAlert: boolean = false;
  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';

  constructor(private vozackaService: VozackaService, private userAuthUtil: AuthTokenUtil) {
    this.userAuthUtil.getUserProfile().subscribe((user) => {
      this.userEmail = user.email;
    })
    this.isEmployer$ = this.userAuthUtil.hasPermission('mup:employer');
  }

  ngOnInit(): void {
    this.isEmployer$ = this.userAuthUtil.hasPermission('mup:employer');
    this.userAuthUtil.getUserProfile().subscribe((data) => {
      this.userEmail = data.email;
      this.ucitajVozacke(this.userEmail);
    })
  }

  ngAfterViewInit(): void {
    if (this.vozackaModalRef && this.vozackaModalRef.nativeElement) {
      this.modalInstance = new bootstrap.Modal(this.vozackaModalRef.nativeElement);
    }
  }

  openVozackaModal(): void {
    if (this.modalInstance) this.modalInstance.show();
    else console.error('Modal nije inicijalizovan!');
  }

  podnesiZahtev(form: NgForm): void {
    if (form.invalid) return;

    const kategorije = this.noviZahtev.kategorijeStr.split(',').map(k => k.trim());
    const payload = { kategorije };

    this.vozackaService.podnesiZahtev(payload, this.userEmail).subscribe({
      next: res => {
        if (this.modalInstance) this.modalInstance.hide();
        form.resetForm();
        this.ucitajVozacke(this.userEmail);
        this.alertType = 'success';
        this.alertMessage = typeof res === 'string' ? res : 'Zahtev uspešno podnet!';
        this.showAlert = true;
      },



      error: err => {
        this.alertType = 'error';
        this.alertMessage = err.error?.message || 'Došlo je do greške pri podnošenju zahteva.';
        this.showAlert = true;
      }
    });
  }


  ucitajVozacke(userEmail: string): void {
    this.vozackaService.dohvatiSve(userEmail).subscribe({
      next: (res) => this.vozacke = res,
      error: (err) => console.error(err)
    });
  }

  closeAlert(): void {
    this.showAlert = false;
  }

  get imaAktivanZahtev(): boolean {
    return this.vozacke.some(z => z.status === 'CEKANJE' || z.status === 'DOZVOLJEN');
  }

  prihvatiZahtev(id: number) {
    this.vozackaService.odobriZahtev(id).subscribe({
      next: res => {
        this.ucitajVozacke(this.userEmail);
        this.alertMessage = res?.message || 'Zahtev je odobren!';
        this.alertType = 'success';
        this.showAlert = true;
      },
      error: err => {
        console.error(err);
        this.alertMessage = err.error?.message || 'Došlo je do greške pri odobravanju zahteva.';
        this.alertType = 'error';
        this.showAlert = true;
      }
    });
  }

  odbijZahtev(id: number) {
    this.vozackaService.odbijZahtev(id).subscribe({
      next: (res: any) => {
        this.ucitajVozacke(this.userEmail);
        this.alertType = 'success';
        this.alertMessage = typeof res === 'string' ? res : 'Zahtev je odbijen!';
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

  produziVozacku() {
    this.vozackaService.produzi(this.userEmail).subscribe({
      next: (res: any) => {
        this.ucitajVozacke(this.userEmail);
        this.alertType = 'success';
        this.alertMessage = res?.message || 'Vozačka je produžena!';
        this.showAlert = true;
      },
      error: err => {
        this.alertType = 'error';
        this.alertMessage = err.error?.message || 'Dozvola još nije spremna za produženje (više od mesec dana do isteka)';
        this.showAlert = true;
      }
    });
  }

  get mozeProduziti(): boolean {
    return this.vozacke.some(z => z.status === 'DOZVOLJEN');
  }

}
