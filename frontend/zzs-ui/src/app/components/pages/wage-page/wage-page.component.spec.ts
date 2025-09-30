import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WagePageComponent } from './wage-page.component';

describe('WagePageComponent', () => {
  let component: WagePageComponent;
  let fixture: ComponentFixture<WagePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WagePageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WagePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
