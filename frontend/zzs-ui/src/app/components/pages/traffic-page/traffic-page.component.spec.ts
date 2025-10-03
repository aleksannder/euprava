import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrafficPageComponent } from './traffic-page.component';

describe('TrafficPageComponent', () => {
  let component: TrafficPageComponent;
  let fixture: ComponentFixture<TrafficPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrafficPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrafficPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
