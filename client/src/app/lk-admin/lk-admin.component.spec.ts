import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LkAdminComponent } from './lk-admin.component';

describe('LkAdminComponent', () => {
  let component: LkAdminComponent;
  let fixture: ComponentFixture<LkAdminComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LkAdminComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LkAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
