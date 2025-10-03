import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GdpStat} from '../model/gdp.model';
import {Region} from '../model/enums/region.enum';
import {GdpGrowth} from '../model/gdp-growth.model';
import {Page} from '../model/page.model';

@Injectable({providedIn: 'root'})
export class GdpService {
  private controllerUrl: string = 'http://localhost:8080/api/gdp';

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 10, sort = 'year,desc'): Observable<Page<GdpStat>> {
    return this.http.get<Page<GdpStat>>(`${this.controllerUrl}?page=${page}&size=${size}&sort=${sort}`);
  }

  getById(id: number): Observable<GdpStat> {
    return this.http.get<GdpStat>(`${this.controllerUrl}/${id}`)
  }

  create(request: GdpStat): Observable<GdpStat> {
    return this.http.post<GdpStat>(`${this.controllerUrl}`, request)
  }

  update(request: GdpStat): Observable<GdpStat> {
    return this.http.put<GdpStat>(`${this.controllerUrl}/${request.id}`, request)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.controllerUrl}/${id}`)
  }

  getGdpTrend(): Observable<GdpStat[]> {
    return this.http.get<GdpStat[]>(`${this.controllerUrl}/trend`);
  }

  getCpi(): Observable<GdpStat[]> {
    return this.http.get<GdpStat[]>(`${this.controllerUrl}/cpi`);
  }

  getGrowthPercent(year: number, region: Region): Observable<GdpGrowth> {
    return this.http.get<GdpGrowth>(`${this.controllerUrl}/growth/${year}/${region}`)
  }

  getByRegion(region: Region, page = 0, size = 10, sort = 'year,desc'): Observable<Page<GdpStat>> {
    return this.http.get<Page<GdpStat>>(`${this.controllerUrl}/region/${region}?page=${page}&size=${size}&sort=${sort}`);
  }
}
