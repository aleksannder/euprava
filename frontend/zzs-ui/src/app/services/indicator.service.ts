import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class IndicatorService {
  private baseUrl: string = 'http://localhost:8080/api/indicators';

  constructor(private http: HttpClient) {}

  getIndicatorsBySubdomainId(subdomainId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/subdomain/${subdomainId}`);
  }
}
