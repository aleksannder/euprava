import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {WageGrowth, WageStat} from '../model/wage.model';
import {Observable} from 'rxjs';
import {Region} from '../model/enums/region.enum';

@Injectable({providedIn: 'root'})
export class WageService {
  private controllerUrl = 'http://localhost:8080/api/wage';

  constructor(private http: HttpClient) {}

  getAll(): Observable<WageStat[]> {
    return this.http.get<WageStat[]>(`${this.controllerUrl}`);
  }

  getById(id: number): Observable<WageStat> {
    return this.http.get<WageStat>(`${this.controllerUrl}/${id}`);
  }

  create(request: WageStat): Observable<WageStat> {
    return this.http.post<WageStat>(`${this.controllerUrl}`, request)
  }

  update(request: WageStat): Observable<WageStat> {
    return this.http.put<WageStat>(`${this.controllerUrl}/${request.id}`, request)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.controllerUrl}/${id}`);
  }

  getWageTrendsByRegion(region: Region): Observable<WageStat[]> {
    return this.http.get<WageStat[]>(`${this.controllerUrl}/trend/${region}`);
  }

  getWageComparisonsByYear(year: number): Observable<WageStat[]> {
    return this.http.get<WageStat[]>(`${this.controllerUrl}/comparison/${year}`);
  }

  getRegionsSortedByAverageWage(): Observable<any[]> {
    return this.http.get<any[]>(`${this.controllerUrl}/ranking`);
  }

  getHighlightByYear(year: number): Observable<WageGrowth> {
    return this.http.get<WageGrowth>(`${this.controllerUrl}/highlight/${year}`);
  }
}
