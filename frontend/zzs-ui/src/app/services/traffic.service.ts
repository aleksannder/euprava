import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {DangerousRegion, FatalitiesTrend, TrafficStat, TrafficSummary} from '../model/traffic.model';
import {Observable} from 'rxjs';
import {PopulationStat} from '../model/population.model';
import {Region} from '../model/enums/region.enum';
import {Page} from '../model/page.model';

@Injectable({providedIn: 'root'})
export class TrafficService {
  private controllerUrl: string = 'http://localhost:8080/api/traffic';

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 10, sort = 'year,desc'): Observable<Page<TrafficStat>> {
    return this.http.get<Page<TrafficStat>>(`${this.controllerUrl}?page=${page}&size=${size}&sort=${sort}`);
  }

  getById(id: number): Observable<TrafficStat> {
    return this.http.get<TrafficStat>(`${this.controllerUrl}/${id}`);
  }

  create(request: TrafficStat): Observable<TrafficStat> {
    return this.http.post<TrafficStat>(`${this.controllerUrl}`, request)
  }

  update(request: TrafficStat): Observable<TrafficStat> {
    return this.http.put<TrafficStat>(`${this.controllerUrl}/${request.id}`, request)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.controllerUrl}/${id}`);
  }

  getTrafficTrendByRegion(region: Region): Observable<TrafficStat[]> {
    return this.http.get<TrafficStat[]>(`${this.controllerUrl}/trend/${region}`);
  }

  getSummary(): Observable<TrafficSummary[]> {
    return this.http.get<TrafficSummary[]>(`${this.controllerUrl}/summary`);
  }

  getDangerousRegions(yearFrom: number): Observable<DangerousRegion[]> {
    return this.http.get<DangerousRegion[]>(`${this.controllerUrl}/dangerous`, {params: {yearFrom}});
  }

  getFatalitiesTrend(): Observable<FatalitiesTrend[]> {
    return this.http.get<FatalitiesTrend[]>(`${this.controllerUrl}/fatalities-trend`);
  }
}
