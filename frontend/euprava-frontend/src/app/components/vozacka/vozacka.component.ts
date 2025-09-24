import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { VozackaDozvola, VozackaService } from '../../services/vozacka.service';
import { NgForm } from '@angular/forms';
import {Role} from "../../models/korisnik";
import {RoleService} from "../../services/role.service";

declare var bootstrap: any;

@Component({
  selector: 'app-vozacka',
  templateUrl: './vozacka.component.html',
  styleUrls: ['./vozacka.component.css']
})
export class VozackaComponent implements OnInit, AfterViewInit {

  @ViewChild('vozackaModal', { static: false }) vozackaModalRef!: ElementRef;

  vozacke: VozackaDozvola[] = [];
  token: string = '';
  rola: Role = Role.CITIZEN;

  noviZahtev = { grad: '', kategorijeStr: '' };
  modalInstance: any;

  showAlert: boolean = false;
  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';

  constructor(private vozackaService: VozackaService, private roleService: RoleService) { }

  ngOnInit(): void {
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

    this.ucitajVozacke();
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

    this.vozackaService.podnesiZahtev(payload).subscribe({
      next: res => {
        if (this.modalInstance) this.modalInstance.hide();
        form.resetForm();
        this.ucitajVozacke();
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


  ucitajVozacke(): void {
    this.vozackaService.dohvatiSve().subscribe({
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

  get isEmployer(): boolean {
    return this.rola === Role.EMPLOYER;
  }

  prihvatiZahtev(id: number) {
    this.vozackaService.odobriZahtev(id).subscribe({
      next: res => {
        this.ucitajVozacke();
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
        this.ucitajVozacke();
        this.alertType = 'success';
        this.alertMessage = typeof res === 'string' ? res : 'Vozačka je produžena!';
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
    this.vozackaService.produzi().subscribe({
      next: (res: any) => {
        this.ucitajVozacke();
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
