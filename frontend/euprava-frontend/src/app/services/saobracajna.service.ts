import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SaobracajnaDozvola {
  id: number;
  ime: string;
  prezime: string;
  adresa: string;
  marka: string;
  model: string;
  kubikaza: number;
  godiste: number;
  vrstaPogona: string;
  tablice: string;
  datumIzdavanja: string;
  datumVazenja: string;
  brojDozvole: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class SaobracajnaService {

  private baseUrl = 'http://localhost:8080/saobracajna-dozvola';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  dohvatiSve(): Observable<SaobracajnaDozvola[]> {
    return this.http.get<SaobracajnaDozvola[]>(`${this.baseUrl}/svi-zahtevi`, { headers: this.getHeaders() });
  }

  podnesiZahtev(payload: any): Observable<string> {
    return this.http.post(`${this.baseUrl}/zahtev`, payload, {
      headers: this.getHeaders(),
      responseType: 'text'
    });
  }

  produzi(): Observable<any> {
    return this.http.post(`${this.baseUrl}/produzi`, {}, { headers: this.getHeaders() });
  }

  odobriZahtev(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/odobri/${id}`, {}, { headers: this.getHeaders() });
  }

  odbijZahtev(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/odbij/${id}`, {}, { headers: this.getHeaders() });
  }

}
