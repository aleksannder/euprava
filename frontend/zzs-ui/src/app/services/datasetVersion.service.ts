import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class DatasetVersionService {
  private baseUrl: string = 'http://localhost:8080/api/datasetVersions';

  constructor(private http: HttpClient) {}

  getAllDatasetVersionsByDatasetId(datasetId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/dataset/${datasetId}`);
  }

}
