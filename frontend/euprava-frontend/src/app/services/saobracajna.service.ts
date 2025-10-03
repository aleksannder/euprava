import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SaobracajnaDozvola {
  id: number;
  firstName: string;
  lastName: string;
  address: string;
  make: string;
  model: string;
  displacement: number;
  manufacturedYear: number;
  fuelType: string;
  plateNumber: string;
  dateOfIssuing: string;
  validUntil: string;
  drivingLicenseNumber: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class SaobracajnaService {

  private baseUrl = 'http://localhost:8081/saobracajna-dozvola';

  constructor(private http: HttpClient) { }

  dohvatiSve(userEmail: string): Observable<SaobracajnaDozvola[]> {
    return this.http.get<SaobracajnaDozvola[]>(`${this.baseUrl}/svi-zahtevi/${userEmail}`);
  }

  podnesiZahtev(payload: any, userEmail: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/zahtev/${userEmail}`, payload, {
      responseType: 'text'
    });
  }

  produzi(userEmail: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/produzi/${userEmail}`, {});
  }

  odobriZahtev(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/odobri/${id}`, {});
  }

  odbijZahtev(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/odbij/${id}`, {});
  }

}
