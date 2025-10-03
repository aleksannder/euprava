import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AvgAgeData, ExtremesData, PopulationStat, ProjectionData, RegionValueDto} from '../model/population.model';
import {Observable} from 'rxjs';
import {Region} from '../model/enums/region.enum';

@Injectable({providedIn: 'root'})
export class PopulationService {
  private controllerUrl: string = 'http://localhost:8080/api/population';

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 10, sort = 'year,desc'): Observable<PopulationStat[]> {
    return this.http.get<PopulationStat[]>(`${this.controllerUrl}`);
  }

  getById(id: number): Observable<PopulationStat> {
    return this.http.get<PopulationStat>(`${this.controllerUrl}/${id}`);
  }

  create(request: PopulationStat): Observable<PopulationStat> {
    return this.http.post<PopulationStat>(`${this.controllerUrl}`, request)
  }

  update(request: PopulationStat) {
    return this.http.put<PopulationStat>(`${this.controllerUrl}/${request.id}`, request)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.controllerUrl}/${id}`);
  }

  getPopulationTrendForRegion(region: Region): Observable<PopulationStat[]> {
    return this.http.get<PopulationStat[]>(`${this.controllerUrl}/trend/${region}`);
  }

  comparePopulationTrendByYear(year: number): Observable<PopulationStat[]> {
    return this.http.get<PopulationStat[]>(`${this.controllerUrl}/compare/${year}`);
  }

  getAverageAgeByRegion(): Observable<AvgAgeData[]> {
    return this.http.get<AvgAgeData[]>(`${this.controllerUrl}/avgAge`);
  }

  getAvgAge(): Observable<RegionValueDto[]> {
    return this.http.get<RegionValueDto[]>(`${this.controllerUrl}/avg-age`);
  }

  getNaturalGrowth(): Observable<RegionValueDto[]> {
    return this.http.get<RegionValueDto[]>(`${this.controllerUrl}/natural-growth`);
  }

  getExtremes(): Observable<ExtremesData> {
    return this.http.get<ExtremesData>(`${this.controllerUrl}/extremes`);
  }

  getProjection(region: Region): Observable<ProjectionData[]> {
    return this.http.get<ProjectionData[]>(`${this.controllerUrl}/projection/${region}`)
  }

  getPopulationShare(year: number): Observable<RegionValueDto[]> {
    return this.http.get<RegionValueDto[]>(`${this.controllerUrl}/share/${year}`);
  }
}
