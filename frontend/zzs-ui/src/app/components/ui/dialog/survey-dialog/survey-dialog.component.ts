import {Component} from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatOption, MatSelect} from '@angular/material/select';
import {NgForOf} from '@angular/common';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {Survey, SurveyDomain} from '../../../../model/survey.model';
import {SurveyService} from '../../../../services/survey.service';
import {SurveyDomainUtil} from '../../../../services/util/domain.util';

@Component({
  selector: 'app-survey-dialog',
  standalone: true,
  imports: [
    MatDialogContent,
    ReactiveFormsModule,
    MatDialogTitle,
    MatFormField,
    MatInput,
    MatSelect,
    MatLabel,
    MatOption,
    NgForOf,
    MatIconButton,
    MatIcon,
    MatButton,
    MatDialogActions
  ],
  templateUrl: './survey-dialog.component.html',
  styleUrl: './survey-dialog.component.scss'
})
export class SurveyDialogComponent {
  form: FormGroup;
  surveyTypes: SurveyDomain[] = Object.values(SurveyDomain);

  constructor(
    private fb: FormBuilder,
    private service: SurveyService,
    protected dialogRef: MatDialogRef<SurveyDialogComponent>
  ) {
    this.form = this.fb.group({
      title: ['', Validators.required],
      type: ['', Validators.required]
    });
  }

  create() {
    if (this.form.invalid) return;
    const { title, type } = this.form.value;
    const survey: Survey = {
      active: true, domain: type, questions: [], title: title, year: 2025
    };
    this.service.createSurvey(survey).subscribe(() => {
      this.dialogRef.close(true);
    });
  }

  protected readonly SurveyDomainUtil = SurveyDomainUtil;
}
