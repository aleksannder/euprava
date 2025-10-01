import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SurveyFillPageComponent } from './survey-fill-page.component';

describe('SurveyFillPageComponent', () => {
  let component: SurveyFillPageComponent;
  let fixture: ComponentFixture<SurveyFillPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SurveyFillPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SurveyFillPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
