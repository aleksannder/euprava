import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GdpGrowth} from '../model/gdp-growth.model';
import {WageGrowth} from '../model/wage.model';
import {ExtremesData} from '../model/population.model';


export interface DashboardStats {
    identificationCards: number;
    vehicleLicences: number;
    driversLicenses: number;
    gunPermits: number;
    gdpGrowth: GdpGrowth;
    highestPaidRegion: WageGrowth;
    extremesDto: ExtremesData;
}

@Injectable(
  {providedIn: 'root'}
)
export class StatsService {
  private baseUrl: string = 'http://localhost:8080/api/dashboard';

  constructor(private http: HttpClient) {}

  getHighlights(userEmail: string): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.baseUrl}/highlights/${userEmail}`);
  }

}
