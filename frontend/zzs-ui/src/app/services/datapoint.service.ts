import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class DataPointService {
  private baseUrl: string = 'http://localhost:8080/api/datapoints';

  constructor(private http: HttpClient) { }

  getDatapointsByIndicatorId(indicatorId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/indicator/${indicatorId}`);
  }

}
