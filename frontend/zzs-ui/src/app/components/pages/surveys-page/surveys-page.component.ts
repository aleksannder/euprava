import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Survey, SurveyWithStatus} from '../../../model/survey.model';
import {SurveyService} from '../../../services/survey.service';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCard, MatCardSubtitle, MatCardTitle} from '@angular/material/card';
import {MatFormField} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatOption, MatSelect} from '@angular/material/select';
import {SurveyDomainUtil} from '../../../services/util/domain.util';
import {Router} from '@angular/router';
import {MatTooltip} from '@angular/material/tooltip';
import {Observable} from 'rxjs';
import {AuthTokenUtil} from '../../../services/auth/auth-token.util';

@Component({
  selector: 'app-surveys-page',
  standalone: true,
  imports: [
    MatCard,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    NgIf,
    MatButton,
    MatSelect,
    MatOption,
    MatCardTitle,
    MatCardSubtitle,
    NgForOf,
    MatTooltip,
    AsyncPipe
  ],
  templateUrl: './surveys-page.component.html',
  styleUrl: './surveys-page.component.scss'
})
export class SurveysPageComponent implements OnInit {
  surveys: SurveyWithStatus[] = [];
  isAnalyst$!: Observable<boolean>;

  constructor(private surveyService: SurveyService, private router: Router, private authUserUtil: AuthTokenUtil) {
    this.isAnalyst$ = authUserUtil.hasPermission('zzs:analyst');
  }

  ngOnInit() {
    this.loadSurveys();
  }

  loadSurveys() {
    this.authUserUtil.getUserProfile().subscribe((user) => {
      this.surveyService.getSurveysWithStatus(user.email).subscribe(data => this.surveys = data);
    })
  }

  closeSurvey(id: number) {
    this.surveyService.closeSurvey(id).subscribe(() => {
      this.loadSurveys();
    });
  }

  openSurveyFill(surveyId: number): void {
    this.router.navigate(['/surveys', surveyId, 'fill']);
  }

  protected readonly SurveyDomainUtil = SurveyDomainUtil;
}
