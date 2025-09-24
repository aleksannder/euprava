import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import {Oruzje, OruzjeService} from "../../services/oruzje.service";
import {Role} from "../../models/korisnik";
import {RoleService} from "../../services/role.service";

declare var bootstrap: any;

@Component({
  selector: 'app-oruzje',
  templateUrl: './oruzje.component.html',
  styleUrls: ['./oruzje.component.css']
})
export class OruzjeComponent implements OnInit, AfterViewInit {

  @ViewChild('oruzjeModal', { static: false }) oruzjeModalRef!: ElementRef;
  kategorijeOpcije: string[] = ['A', 'B', 'C', 'D', 'E'];

  oruzja: Oruzje[] = [];
  token: string = '';
  rola: Role = Role.CITIZEN;

  noviZahtev: any = { kategorijaOruzja: [] };
  modalInstance: any;

  showAlert: boolean = false;
  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';

  constructor(private oruzjeService: OruzjeService, private roleService: RoleService) { }

  ngOnInit(): void {
    this.token = localStorage.getItem('jwtToken') || '';
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

    this.ucitajOruzja();
  }

  ngAfterViewInit(): void {
    if (this.oruzjeModalRef && this.oruzjeModalRef.nativeElement) {
      this.modalInstance = new bootstrap.Modal(this.oruzjeModalRef.nativeElement);
    }
  }

  openOruzjeModal(): void {
    if (this.modalInstance) this.modalInstance.show();
  }

  podnesiZahtev(form: NgForm): void {
    if (form.invalid) return;

    const danasnjiDatum = new Date().toISOString().split('T')[0]; // "YYYY-MM-DD"

    const payload = {
      datumOd: danasnjiDatum,
      kategorijaOruzja: this.noviZahtev.kategorijaOruzja
    };

    this.oruzjeService.podnesiZahtev(payload).subscribe({
      next: res => {
        if (this.modalInstance) this.modalInstance.hide();
        form.resetForm();
        this.noviZahtev = { kategorijaOruzja: [] };
        this.ucitajOruzja();
        this.alertType = 'success';
        this.alertMessage = res?.message || 'Zahtev uspešno podnet!';
        this.showAlert = true;
      },
      error: err => {
        this.alertType = 'error';
        this.alertMessage = err.error?.message || 'Došlo je do greške pri podnošenju zahteva.';
        this.showAlert = true;
      }
    });
  }

  ucitajOruzja(): void {
    this.oruzjeService.dohvatiSve().subscribe({
      next: res => this.oruzja = res,
      error: err => console.error(err)
    });
  }

  closeAlert(): void {
    this.showAlert = false;
  }

  get imaAktivanZahtev(): boolean {
    return this.oruzja.some(z => z.status === 'CEKANJE' || z.status === 'DOZVOLJEN');
  }

  get isEmployer(): boolean {
    return this.rola === Role.EMPLOYER;
  }

  prihvatiZahtev(id: number) {
    this.oruzjeService.odobriZahtev(id).subscribe({
      next: res => {
        this.ucitajOruzja();
        this.alertType = 'success';
        this.alertMessage = res?.message || 'Zahtev odobren!';
        this.showAlert = true;
      },
      error: err => {
        this.alertType = 'error';
        this.alertMessage = err.error?.message || 'Greška pri odobravanju zahteva.';
        this.showAlert = true;
      }
    });
  }

  odbijZahtev(id: number) {
    this.oruzjeService.odbijZahtev(id).subscribe({
      next: res => {
        this.ucitajOruzja();
        this.alertType = 'success';
        this.alertMessage = res?.message || 'Zahtev odbijen!';
        this.showAlert = true;
      },
      error: err => {
        this.alertType = 'error';
        this.alertMessage = err.error?.message || 'Greška pri odbijanju zahteva.';
        this.showAlert = true;
      }
    });
  }

  produziSveZahteve() {
    this.oruzjeService.produziZahteve().subscribe({
      next: res => {
        this.ucitajOruzja();
        this.alertType = 'success';
        this.alertMessage = res?.message || 'Svi zahtevi uspešno produženi!';
        this.showAlert = true;
      },
      error: err => {
        this.alertType = 'error';
        this.alertMessage = err.error?.message || 'Došlo je do greške pri produženju zahteva.';
        this.showAlert = true;
      }
    });
  }

  get mozeProduziti(): boolean {
    return !this.isEmployer && this.oruzja.some(o => o.status === 'DOZVOLJEN');
  }

}
