import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Survey, SurveyResponse, SurveyWithStatus} from '../model/survey.model';

@Injectable({ providedIn: 'root' })
export class SurveyService {
  private baseUrl = 'http://localhost:8080/api/surveys';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Survey[]> {
    return this.http.get<Survey[]>(this.baseUrl);
  }

  getById(surveyId: number): Observable<Survey> {
    return this.http.get<Survey>(`${this.baseUrl}/${surveyId}`);
  };

  createSurvey(survey: Survey): Observable<Survey> {
    return this.http.post<Survey>(this.baseUrl, survey);
  }

  submitResponses(id: number, responses: SurveyResponse[]): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${id}/responses`, responses);
  }

  closeSurvey(id: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${id}/close`, {});
  }

  hasUserResponded(surveyId: number, userEmail: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/${surveyId}/responded/${userEmail}`);
  }

  getSurveysWithStatus(userEmail: string): Observable<SurveyWithStatus[]> {
    return this.http.get<SurveyWithStatus[]>(`${this.baseUrl}/with-status`, {
      params: { userEmail: userEmail },
    });
  }
}
