import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Oruzje {
  id: number;
  ime: string;
  prezime: string;
  datumOd: string;
  datumDo: string;
  regBroj: string;
  kategorijaOruzja: string;
  status: 'CEKANJE' | 'DOZVOLJEN' | 'ODBIJEN'| 'IZGUBLJEN';
}

@Injectable({
  providedIn: 'root'
})
export class OruzjeService {

  private baseUrl = 'http://localhost:8080/oruzje';

  constructor(private http: HttpClient) { }

  podnesiZahtev(zahtev: Partial<Oruzje>): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.baseUrl}/zahtev`, zahtev);
  }

  dohvatiSve(): Observable<Oruzje[]> {
    return this.http.get<Oruzje[]>(`${this.baseUrl}/svi-zahtevi`);
  }

  odobriZahtev(id: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(`${this.baseUrl}/odobri/${id}`, {});
  }

  odbijZahtev(id: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(`${this.baseUrl}/odbij/${id}`, {});
  }

  produziZahteve(ids?: number[]) {
    return this.http.put<any>(`${this.baseUrl}/produzi-sve`, {});
  }

}
