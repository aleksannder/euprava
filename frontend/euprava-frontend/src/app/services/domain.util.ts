import {SurveyDomain} from "../models/survey.model";

export class DomainUtil {
  static getLabel(surveyDomain: SurveyDomain): string {
    switch (surveyDomain) {
      case SurveyDomain.POPULATION:
        return 'Stanovništvo';
      case SurveyDomain.GDP:
        return 'Ekonomija';
      case SurveyDomain.TRAFFIC:
        return 'Saobraćaj';
      case SurveyDomain.WAGE:
        return 'Plate';
      default:
        return surveyDomain;
    }
  }
}
