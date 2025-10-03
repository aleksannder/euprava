import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Survey, SurveyResponse, SurveyWithStatus} from "../models/survey.model";

@Injectable({providedIn: 'root'})
export class SurveyService {
  private baseUrl: string = 'http://localhost:8081/api/surveys';

  constructor(private http: HttpClient) {}

  getSurveysWithStatus(userEmail: string): Observable<SurveyWithStatus[]> {
    return this.http.get<SurveyWithStatus[]>(`${this.baseUrl}/with-status?userEmail=${userEmail}`, {params: { userEmail: userEmail }});
  }

  fillSurvey(surveyResponse: SurveyResponse, userEmail: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/{surveyId}/responses`, surveyResponse, {params: {userEmail: userEmail}});
  }

  getById(surveyId: string): Observable<Survey> {
    return this.http.get<Survey>(`${this.baseUrl}/surveys/${surveyId}`);
  }

  hasUserResponded(surveyId: number, userEmail: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/${surveyId}/responded/${userEmail}`);
  }
}
