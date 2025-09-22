import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RegisterUserRequest, RegisterUserResponse} from '../../model/register-user.model';
import {Observable} from 'rxjs';


@Injectable()
export class AuthService {
    private controllerUrl: string = 'http://localhost:8080/api/auth';

    constructor(private httpClient: HttpClient) {}


    registerUser(registerUserRequest: RegisterUserRequest): Observable<RegisterUserResponse> {
      return this.httpClient.post<RegisterUserResponse>(`${this.controllerUrl}/register`, registerUserRequest);
    }
}
