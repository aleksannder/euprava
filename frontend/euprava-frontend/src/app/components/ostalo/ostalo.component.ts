import { Component, OnInit } from '@angular/core';
import { ZahteviService, UnifiedZahtevResponse } from '../../services/zahtevi.service';
import {Role} from "../../models/korisnik";
import {RoleService} from "../../services/role.service";
import {Observable} from "rxjs";
import {AuthTokenUtil} from "../../interceptor/auth-token.util";

interface UnifiedItem {
  id: string | number;
  tip: string;
  status?: string;
  datumPodnosenja?: string;
  dodatniPodaci?: string;
}

@Component({
  selector: 'app-ostalo',
  templateUrl: './ostalo.component.html',
  styleUrls: ['./ostalo.component.css']
})
export class OstaloComponent implements OnInit {
  zahtevi: UnifiedItem[] = [];
  message = '';
  alertMessage: string = '';
  isEmployer$!: Observable<boolean>;
  userEmail!: string;
  alertType: 'success' | 'error' = 'success';
  showAlert: boolean = false;
  maticnaPodaci: any = null;
  prikaziMaticnu: boolean = false;

  constructor(private zahteviService: ZahteviService, private authUtil: AuthTokenUtil) {}

  ngOnInit(): void {
    this.authUtil.getUserProfile().subscribe((user) => {
      this.userEmail = user.email;
      this.ucitajZahteve(this.userEmail);
      this.isEmployer$ = this.authUtil.hasPermission('mup:employer');
    })
  }

  selectedDokument: string = '';
  aktivniDokumenti: UnifiedItem[] = [];
  noviZahtev: { tip: string; datumPrijave: string } = { tip: '', datumPrijave: '' };

  ucitajZahteve(userEmail: string): void {
    this.message = 'Učitavam...';
    this.zahteviService.getMojiZahtevi(userEmail).subscribe({
      next: (data) => {
        this.zahtevi = this.normalize(data);
        this.sortByDateDesc();
        this.aktivniDokumenti = this.zahtevi.filter(z => z.status === 'DOZVOLJEN'); // samo aktivni dokumenti
        this.message = `Učitano ${this.zahtevi.length} zahteva.`;
      },
      error: (err) => {
        this.message = `Greška: ${err.message}`;
      }
    });
  }

  prijaviIzgubljeniDokument(): void {
    if (!this.selectedDokument) return;

    const dokument = this.aktivniDokumenti.find(d => d.tip === this.selectedDokument);
    if (!dokument) return;

    const danasnjiDatum = new Date().toISOString().split('T')[0];

    const payload = {
      tip: dokument.tip,
      id: dokument.id,
      datumPrijave: danasnjiDatum
    };

    this.zahteviService.prijaviIzgubljeniDokument(payload, this.userEmail).subscribe({
      next: (res) => {
        this.alertMessage = res.message;
        this.alertType = 'success';
        this.showAlert = true;
        this.ucitajZahteve(this.userEmail);
        this.selectedDokument = '';
      },
      error: (err) => {
        this.alertMessage = "Greška prilikom prijave: " + (err.error?.message || err.message);
        this.alertType = 'error';
        this.showAlert = true;
      }
    });
  }

  private sortByDateDesc(): void {
    this.zahtevi.sort((a, b) => {
      const dateA = a.datumPodnosenja ? new Date(a.datumPodnosenja).getTime() : 0;
      const dateB = b.datumPodnosenja ? new Date(b.datumPodnosenja).getTime() : 0;
      return dateB - dateA;
    });
  }

  private normalize(data: UnifiedZahtevResponse): UnifiedItem[] {
    const out: UnifiedItem[] = [];

    (data.licne_karte || []).forEach(x =>
      out.push({
        id: x.id ?? 'n/a',
        tip: 'Lična karta',
        status: x.status,
        datumPodnosenja: x.datumIzdavanja
      })
    );

    (data.oruzje || []).forEach(x =>
      out.push({
        id: x.id ?? 'n/a',
        tip: 'Oružje',
        status: x.status,
        datumPodnosenja: x.datumOd
      })
    );

    (data.saobracajne_dozvole || []).forEach(x =>
      out.push({
        id: x.id ?? 'n/a',
        tip: 'Saobraćajna dozvola',
        status: x.status,
        datumPodnosenja: x.datumIzdavanja
      })
    );

    (data.vozacke_dozvole || []).forEach(x =>
      out.push({
        id: x.id ?? 'n/a',
        tip: 'Vozačka dozvola',
        status: x.status,
        datumPodnosenja: x.datumIzdavanja
      })
    );

    return out;
  }

  closeAlert(): void {
    this.showAlert = false;
  }

  toggleMaticna(): void {
    this.prikaziMaticnu = !this.prikaziMaticnu;

    if (this.prikaziMaticnu && !this.maticnaPodaci) {
      // poziv servisa za preuzimanje podataka
      this.zahteviService.getMaticnaKnjiga(this.userEmail).subscribe({
        next: (data) => {
          this.maticnaPodaci = data;
        },
        error: (err) => {
          this.alertMessage = "Greška prilikom učitavanja podataka iz matične knjige.";
          this.alertType = 'error';
          this.showAlert = true;
        }
      });
    }
  }

}
