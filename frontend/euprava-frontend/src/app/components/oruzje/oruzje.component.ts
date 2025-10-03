import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import {Oruzje, OruzjeService} from "../../services/oruzje.service";

import {BehaviorSubject, Observable} from "rxjs";
import {AuthTokenUtil} from "../../interceptor/auth-token.util";

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
  isEmployer$!: Observable<boolean>;
  mozeProduziti$ = new BehaviorSubject<boolean>(false);
  noviZahtev: any = { gunCategories: [] };
  modalInstance: any;
  canProduce: boolean = false;
  userEmail!: string;

  showAlert: boolean = false;
  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';

  constructor(private oruzjeService: OruzjeService, private authUtil: AuthTokenUtil) {
    this.isEmployer$ = this.authUtil.hasPermission('mup:employer');
  }

  ngOnInit(): void {
    this.authUtil.getUserProfile().subscribe((user) => {
      this.userEmail = user.email;
      this.ucitajOruzja();
    })

    this.isEmployer$.subscribe(isEmployer => {
      if (!isEmployer) {
        this.canProduce = this.oruzja.some(o => o.status === 'DOZVOLJEN');
      } else {
        this.canProduce = false;
      }
      this.mozeProduziti$.next(this.canProduce);
    });
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
      dateFrom: danasnjiDatum,
      gunCategories: this.noviZahtev.gunCategories
    };

    this.oruzjeService.podnesiZahtev(payload, this.userEmail).subscribe({
      next: res => {
        if (this.modalInstance) this.modalInstance.hide();
        form.resetForm();
        this.noviZahtev = { gunCategories: [] };
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
    this.oruzjeService.dohvatiSve(this.userEmail).subscribe({
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
    return this.mozeProduziti$.value;
  }

}
