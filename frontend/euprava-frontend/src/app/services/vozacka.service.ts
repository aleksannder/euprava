import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface VozackaDozvola {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  dateOfIssuing: string;
  validUntil: string;
  city: string;
  licenseNumber: string;
  categories: string[];
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class VozackaService {

  private baseUrl = 'http://localhost:8081/vozacka-dozvola';

  constructor(private http: HttpClient) { }

  dohvatiSve(userEmail: string): Observable<VozackaDozvola[]> {
    return this.http.get<VozackaDozvola[]>(`${this.baseUrl}/svi-zahtevi/${userEmail}`);
  }

  podnesiZahtev(payload: { kategorije: string[] }, userEmail: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/zahtev/${userEmail}`, payload, { responseType: 'text'});
  }

  produzi(userEmail: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/produzi/${userEmail}`, {}, { responseType: 'text' });
  }

  odobriZahtev(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/odobri/${id}`, {});
  }

  odbijZahtev(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/odbij/${id}`, {});
  }

}
