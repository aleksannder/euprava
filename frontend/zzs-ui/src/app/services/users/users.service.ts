import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RegisterUserRequest} from '../../model/register-user.model';
import {Observable} from 'rxjs';

@Injectable()
export class UsersService {
  private baseUrl: string = 'http://localhost:8080/api/auth';
  constructor(private http: HttpClient) {}

  register(req: RegisterUserRequest): Observable<{ success: boolean }> {
    return this.http.post<{ success: boolean }>(`${this.baseUrl}/register`, req);
  }
}
