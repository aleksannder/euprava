import {Component, OnInit} from "@angular/core";
import {SurveyWithStatus} from "../../models/survey.model";
import {DomainUtil} from "../../services/domain.util";
import {Observable} from "rxjs";
import {AuthTokenUtil} from "../../interceptor/auth-token.util";
import {SurveyService} from "../../services/survey.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-surveys',
  templateUrl: './surveys.component.html',
  styleUrls: ['./surveys.component.css']
})
export class SurveysComponent implements OnInit {
    surveys: SurveyWithStatus[] = [];
    isEmployer$: Observable<boolean>;
    userEmail: string = '';


    constructor(private authUtil: AuthTokenUtil, private surveyService: SurveyService, private router: Router) {
      this.isEmployer$ = this.authUtil.hasPermission('mup:employer');
    }

    ngOnInit(): void {
      this.authUtil.getUserProfile().subscribe((user) => {
        this.userEmail = user.email;
        this.surveyService.getSurveysWithStatus(this.userEmail).subscribe((data) => {
          this.surveys = data;
        })
      })

        this.isEmployer$ = this.authUtil.hasPermission('mup:employer');
    }

    openSurvey(s: SurveyWithStatus): void {
      window.open(`http://localhost:4200/surveys/${s.id}/fill`, '_blank');
    }
  protected readonly DomainUtil = DomainUtil;
}
