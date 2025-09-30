import {Region} from './enums/region.enum';

export interface SurveyResponse {
  id?: number;
  surveyId: number;
  region: Region;
  responseData: any; // JSON field with answers
}
