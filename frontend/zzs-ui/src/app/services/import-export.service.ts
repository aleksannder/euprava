import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class ImportExportService {
  private baseUrl: string = 'http://localhost:8080/api/import-export';

  constructor(private http: HttpClient) {}

  uploadCsv(domain: string, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.baseUrl}/${domain}/import`, formData, {})
  }

  exportCsv(domain: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${domain}/export`, {
      responseType: 'blob',
    });
  }


}
