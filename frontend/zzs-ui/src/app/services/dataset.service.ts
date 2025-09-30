import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class DatasetService {
  private baseUrl: string = 'http://localhost:8080/api/datasets';

  constructor(private http: HttpClient) {}

  getAllDatasets(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

}
