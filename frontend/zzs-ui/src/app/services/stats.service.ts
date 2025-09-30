import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable(
  {providedIn: 'root'}
)
export class StatsService {
  private baseUrl: string = 'http://localhost:8080/api/stats';

  constructor(private http: HttpClient) {}

  getHighlights(): Observable<any> {
    return this.http.get(`${this.baseUrl}/highlights`);
  }

}
