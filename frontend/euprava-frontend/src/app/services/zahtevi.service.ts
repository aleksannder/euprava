import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {LicnaKarta} from "../models/LicnaKarta";
export interface UnifiedZahtevResponse {
  licne_karte: any[];
  oruzje: any[];
  saobracajne_dozvole: any[];
  vozacke_dozvole: any[];
}
@Injectable({
  providedIn: 'root'
})
export class ZahteviService {

  private baseUrl = 'http://localhost:8081/licna-karta';
  private baseUrl1 = 'http://localhost:8081/zahtevi';

  constructor(private http: HttpClient) { }

  getSviZahtevi(token: string): Observable<LicnaKarta[]> {
    return this.http.get<LicnaKarta[]>(`${this.baseUrl}/svi-zahtevi/${token}`);
  }

  podnesiZahtev(token: string, zahtev: { jmbg: string; datumRodjenja: string; pol: string; grad: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/zahtev/${token}`, zahtev, { responseType: 'text' });
  }

  prihvatiZahtev(id: number): Observable<any> {
    return this.http.put(`http://localhost:8081/licna-karta/odobri/${id}`, {}, {
      responseType: 'text'
    });
  }

  odbijZahtev(id: number): Observable<any> {
    return this.http.put(`http://localhost:8081/licna-karta/odbij/${id}`, {}, {
      responseType: 'text'
    });
  }

  produziLicnu(userEmail: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/produzi/${userEmail}`, {}, {responseType: 'text' });
  }

  getMojiZahtevi(userEmail: string): Observable<UnifiedZahtevResponse> {
    return this.http.get<UnifiedZahtevResponse>(`${this.baseUrl1}/moji/${userEmail}`);
  }

  prijaviIzgubljeniDokument(payload: { tip: string; id: string | number; datumPrijave: string }, userEmail: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.baseUrl1}/prijava-izgubljenog/${userEmail}`, payload);
  }

  getMaticnaKnjiga(userEmail: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl1}/maticna-knjiga/${userEmail}`);
  }

}
