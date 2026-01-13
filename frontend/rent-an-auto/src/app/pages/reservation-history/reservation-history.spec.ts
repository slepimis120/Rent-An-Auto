import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationHistory } from './reservation-history';

describe('ReservationHistory', () => {
  let component: ReservationHistory;
  let fixture: ComponentFixture<ReservationHistory>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReservationHistory]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationHistory);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
