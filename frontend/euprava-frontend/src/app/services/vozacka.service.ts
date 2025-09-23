import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface VozackaDozvola {
  id: number;
  ime: string;
  prezime: string;
  datumRodjenja: string;
  datumIzdavanja: string;
  datumVazenja: string;
  grad: string;
  brojDozvole: string;
  kategorije: string[];
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class VozackaService {

  private baseUrl = 'http://localhost:8080/vozacka-dozvola';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  dohvatiSve(): Observable<VozackaDozvola[]> {
    return this.http.get<VozackaDozvola[]>(`${this.baseUrl}/svi-zahtevi`, { headers: this.getHeaders() });
  }

  podnesiZahtev(payload: { kategorije: string[] }): Observable<any> {
    return this.http.post(`${this.baseUrl}/zahtev`, payload, { headers: this.getHeaders() ,responseType: 'text'});
  }

  produzi(): Observable<any> {
    return this.http.post(`${this.baseUrl}/produzi`, {}, { headers: this.getHeaders(),responseType: 'text' });
  }

  odobriZahtev(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/odobri/${id}`, {}, { headers: this.getHeaders() });
  }

  odbijZahtev(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/odbij/${id}`, {}, { headers: this.getHeaders() });
  }

  proveriDaLiJeEmployer(): boolean {
    const role = localStorage.getItem('role');
    return role === 'EMPLOYER';
  }
}
