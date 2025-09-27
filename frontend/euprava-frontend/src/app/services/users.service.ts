import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Korisnik} from "../models/korisnik";
import {Observable} from "rxjs";

@Injectable()
export class UsersService {
  private baseUrl: string = 'http://localhost:8081/api/auth';
  constructor(private http: HttpClient) { }

  register(req: Korisnik): Observable<{success: boolean}> {
    return this.http.post<{success: boolean}>(`${this.baseUrl}/register`, req);
  }
}
