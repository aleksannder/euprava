import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GdpPageComponent } from './gdp-page.component';

describe('GdpPageComponent', () => {
  let component: GdpPageComponent;
  let fixture: ComponentFixture<GdpPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GdpPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GdpPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
