import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Domain} from '../../model/domain.model';
import {Observable} from 'rxjs';

@Injectable()
export class DomainService {
  private baseUrl: string = `http://localhost:8080/api/domain`;

  constructor(private http: HttpClient) { }

  public getDomainById(domainId: number): Observable<Domain> {
    return this.http.get<Domain>(`${this.baseUrl}/${domainId}`);
  }

  public getAllDomains(): Observable<Domain[]> {
    return this.http.get<Domain[]>(`${this.baseUrl}`);
  }

  public deleteDomain(domainId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${domainId}`);
  }
  
}
