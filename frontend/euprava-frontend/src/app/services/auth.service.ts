import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

export interface Korisnik {
  ime: string;
  prezime: string;
  email: string;
  lozinka: string;
  rola?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/api/auth';
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) { }

  private jsonHeaders = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    }),
    withCredentials: true
  };

  register(korisnik: Korisnik): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, korisnik, this.jsonHeaders);
  }

  login(email: string, lozinka: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, { email, lozinka }, this.jsonHeaders);
  }

  logout(): Observable<void> {
    localStorage.removeItem('jwtToken');
    return of(void 0);
  }

  getRoleFromToken(): string {
    const token = localStorage.getItem('jwtToken');
    if (!token) return '';

    const decodedToken = this.jwtHelper.decodeToken(token);
    return decodedToken.role || decodedToken.rola || '';
  }

  getEmailFromToken(): string {
    const token = localStorage.getItem('jwtToken');
    if (!token) return '';
    const decodedToken = this.jwtHelper.decodeToken(token);
    return decodedToken.sub || decodedToken.email || '';
  }

  getCurrentUser(): Observable<Korisnik> {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      throw new Error("Token nije pronađen");
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Korisnik>(`${this.baseUrl}/me`, { headers });
  }

}
