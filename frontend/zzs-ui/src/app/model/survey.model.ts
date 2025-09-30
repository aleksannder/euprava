import {Region} from './enums/region.enum';

export enum SurveyDomain {
  WAGE = 'WAGE',
  TRAFFIC = 'TRAFFIC',
  POPULATION = 'POPULATION',
  GDP = 'GDP'
}

export interface Survey {
  id?: number;
  title: string;
  year: number;
  domain: SurveyDomain;
  active: boolean;
  questions: SurveyQuestion[];
}

export interface SurveyQuestion {
  id?: number;
  text: string;
  type: 'NUMBER' | 'YES_NO';
}

export interface SurveyResponse {
  id?: number;
  userEmail: string;
  region: Region;
  survey: Survey;
  question: SurveyQuestion;
  answer: string;
}

export interface SurveyWithStatus {
  id: number;
  title: string;
  domain: SurveyDomain;
  active: boolean;
  responded: boolean;
}
