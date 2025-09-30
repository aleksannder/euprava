import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable(
  {providedIn: 'root'}
)
export class SubdomainService {
  private baseUrl: string = 'http://localhost:8080/api/subdomains';

  constructor(private http: HttpClient) {}

  getSubdomainByCode(code: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/code/${code}`);
  }

}
