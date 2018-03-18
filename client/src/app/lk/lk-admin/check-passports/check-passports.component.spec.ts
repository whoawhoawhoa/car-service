import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckPassportsComponent } from './check-passports.component';

describe('CheckPassportsComponent', () => {
  let component: CheckPassportsComponent;
  let fixture: ComponentFixture<CheckPassportsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckPassportsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckPassportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
