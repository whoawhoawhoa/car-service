import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerPassportsComponent } from './worker-passports.component';

describe('WorkerPassportsComponent', () => {
  let component: WorkerPassportsComponent;
  let fixture: ComponentFixture<WorkerPassportsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkerPassportsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkerPassportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
