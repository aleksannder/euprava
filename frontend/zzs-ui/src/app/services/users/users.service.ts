import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RegisterUserRequest, UserInfo, UserUpdateRequest} from '../../model/register-user.model';
import {Observable} from 'rxjs';
import {CitizenDashboard} from '../../model/citizen-dashboard.model';

@Injectable()
export class UsersService {
  private baseUrl: string = 'http://localhost:8080/api/auth';
  private userUrl: string = 'http://localhost:8080/api/users';
  constructor(private http: HttpClient) {}

  register(req: RegisterUserRequest): Observable<{ success: boolean }> {
    return this.http.post<{ success: boolean }>(`${this.baseUrl}/register`, req);
  }

  getUserDashboardInfo(): Observable<CitizenDashboard> {
    return this.http.get<CitizenDashboard>('http://localhost:8080/api/dashboard/highlights');
  }

  getUserInfo(userEmail: string): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.userUrl}/${userEmail}`);
  }

  updateUserInfo(userUpdateRequest: UserUpdateRequest, email: string): Observable<UserInfo> {
    return this.http.post<UserInfo>(`${this.userUrl}/${email}/update`, userUpdateRequest);
  }
}
