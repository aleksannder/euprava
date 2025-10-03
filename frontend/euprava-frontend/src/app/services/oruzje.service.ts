import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthTokenUtil} from "../interceptor/auth-token.util";

export interface Oruzje {
  id: number;
  firstName: string;
  lastName: string;
  dateFrom: string;
  dateTo: string;
  registrationNumber: string;
  gunCategories: string;
  status: 'CEKANJE' | 'DOZVOLJEN' | 'ODBIJEN'| 'IZGUBLJEN';
}

@Injectable({
  providedIn: 'root'
})
export class OruzjeService {

  private baseUrl = 'http://localhost:8081/oruzje';
  constructor(private http: HttpClient) {
  }

  podnesiZahtev(zahtev: Partial<Oruzje>, userEmail: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.baseUrl}/zahtev/${userEmail}`, zahtev);
  }

  dohvatiSve(userEmail: string): Observable<Oruzje[]> {
    return this.http.get<Oruzje[]>(`${this.baseUrl}/svi-zahtevi/${userEmail}`);
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
