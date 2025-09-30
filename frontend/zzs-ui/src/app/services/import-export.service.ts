import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class ImportExportService {
  private baseUrl: string = 'http://localhost:8080/api/datapoints';

  constructor(private http: HttpClient) {}

  uploadCsv(datasetVersionId: number, indicatorId: number, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.baseUrl}/import/${datasetVersionId}/${indicatorId}`, formData, {})
  }


}
