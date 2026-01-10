import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeMerchantComponent } from './home-merchant.component';

describe('HomeMerchantComponent', () => {
  let component: HomeMerchantComponent;
  let fixture: ComponentFixture<HomeMerchantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeMerchantComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeMerchantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
