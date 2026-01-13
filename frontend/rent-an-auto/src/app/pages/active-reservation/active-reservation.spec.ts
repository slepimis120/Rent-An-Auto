import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveReservation } from './active-reservation';

describe('ActiveReservation', () => {
  let component: ActiveReservation;
  let fixture: ComponentFixture<ActiveReservation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ActiveReservation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActiveReservation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
