import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRegComponent } from './admin-reg.component';

describe('AdminRegComponent', () => {
  let component: AdminRegComponent;
  let fixture: ComponentFixture<AdminRegComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminRegComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminRegComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
