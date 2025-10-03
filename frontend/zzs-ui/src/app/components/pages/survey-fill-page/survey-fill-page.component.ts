import {Component, OnInit} from '@angular/core';
import {Survey, SurveyQuestion, SurveyResponse} from '../../../model/survey.model';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Region} from '../../../model/enums/region.enum';
import {ActivatedRoute, Router} from '@angular/router';
import {SurveyService} from '../../../services/survey.service';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatOption, MatSelect} from '@angular/material/select';
import {NgForOf, NgIf} from '@angular/common';
import {MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {SurveyDomainUtil} from '../../../services/util/domain.util';
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';
import {RegionUtil} from '../../../services/util/region.util';
import {AuthTokenUtil} from '../../../services/auth/auth-token.util';

@Component({
  selector: 'app-survey-fill-page',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormField,
    MatSelect,
    MatOption,
    NgForOf,
    MatLabel,
    MatInput,
    MatButton,
    NgIf,
    MatRadioGroup,
    MatRadioButton
  ],
  templateUrl: './survey-fill-page.component.html',
  styleUrl: './survey-fill-page.component.scss'
})
export class SurveyFillPageComponent implements OnInit {
    survey?: Survey;
    surveyForm!: FormGroup;
    hasResponded = false;
    regions: Region[] = Object.values(Region);
    userEmail!: string;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private surveyService: SurveyService,
    private router: Router,
    private auth: AuthTokenUtil
  ) {}

    ngOnInit(): void {
      const surveyId = Number(this.route.snapshot.paramMap.get('id'));
      if (!surveyId) return;

       this.auth.getUserProfile().subscribe((user) => {
         this.userEmail = user.email;
         this.surveyService.hasUserResponded(surveyId, this.userEmail).subscribe(already => {
           this.hasResponded = already;
           if (!already) {
             this.loadSurvey(surveyId);
           }
         });
       })
    }

  loadSurvey(surveyId: number) {
    this.surveyService.getById(surveyId).subscribe(survey => {
      this.survey = survey;

      const controls: any = {};
      survey.questions.forEach(q => {
        controls[`q_${q.id}`] = ['', Validators.required];
      });
      controls['region'] = ['', Validators.required];

      this.surveyForm = this.fb.group(controls);
    });
  }

  onSubmit(): void {
    if (!this.survey || this.surveyForm.invalid) return;


    const responses: SurveyResponse[] = this.survey.questions.map((q: SurveyQuestion) => ({
      userEmail: this.userEmail,
      region: this.surveyForm.value['region'] as Region,
      survey: this.survey!,
      question: q,
      answer: this.surveyForm.value[`q_${q.id}`]
    }));

    this.surveyService.submitResponses(this.survey.id!, responses).subscribe(() => {
      alert('Hvala na popunjavanju ankete!');
      this.router.navigate(['/surveys']);
    });
  }
    protected readonly SurveyDomainUtil = SurveyDomainUtil;
  protected readonly RegionUtil = RegionUtil;
}
