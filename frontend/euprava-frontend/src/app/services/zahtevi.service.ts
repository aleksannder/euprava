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

  private baseUrl = 'http://localhost:8080/licna-karta';
  private baseUrl1 = 'http://localhost:8080/zahtevi';

  constructor(private http: HttpClient) { }

  getSviZahtevi(token: string): Observable<LicnaKarta[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<LicnaKarta[]>(`${this.baseUrl}/svi-zahtevi`, { headers });
  }

  podnesiZahtev(token: string, zahtev: { jmbg: string; datumRodjenja: string; pol: string; grad: string }): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post(`${this.baseUrl}/zahtev`, zahtev, { headers, responseType: 'text' });
  }

  prihvatiZahtev(token: string, id: number): Observable<any> {
    return this.http.put(`http://localhost:8080/licna-karta/odobri/${id}`, {}, {
      headers: { 'Authorization': `Bearer ${token}` },
      responseType: 'text'
    });
  }

  odbijZahtev(token: string, id: number): Observable<any> {
    return this.http.put(`http://localhost:8080/licna-karta/odbij/${id}`, {}, {
      headers: { 'Authorization': `Bearer ${token}` },
      responseType: 'text'
    });
  }

  produziLicnu(token: string): Observable<string> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post(`${this.baseUrl}/produzi`, {}, { headers, responseType: 'text' });
  }

  getMojiZahtevi(): Observable<UnifiedZahtevResponse> {
    return this.http.get<UnifiedZahtevResponse>(`${this.baseUrl1}/moji`);
  }

  prijaviIzgubljeniDokument(payload: { tip: string; id: string | number; datumPrijave: string }): Observable<{ message: string }> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('token')}`);
    return this.http.post<{ message: string }>(`${this.baseUrl1}/prijava-izgubljenog`, payload, { headers });
  }

  getMaticnaKnjiga(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl1}/maticna-knjiga`);
  }

}
