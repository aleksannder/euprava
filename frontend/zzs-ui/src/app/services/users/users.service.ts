import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RegisterUserRequest} from '../../model/register-user.model';
import {Observable} from 'rxjs';
import {CitizenDashboard} from '../../model/citizen-dashboard.model';

@Injectable()
export class UsersService {
  private baseUrl: string = 'http://localhost:8080/api/auth';
  constructor(private http: HttpClient) {}

  register(req: RegisterUserRequest): Observable<{ success: boolean }> {
    return this.http.post<{ success: boolean }>(`${this.baseUrl}/register`, req);
  }

  getUserDashboardInfo(): Observable<CitizenDashboard> {
    return this.http.get<CitizenDashboard>('http://localhost:8080/api/dashboard/my-region');
  }
}
