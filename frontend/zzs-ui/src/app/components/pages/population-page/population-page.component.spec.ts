import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PopulationPageComponent } from './population-page.component';

describe('PopulationPageComponent', () => {
  let component: PopulationPageComponent;
  let fixture: ComponentFixture<PopulationPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PopulationPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PopulationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
