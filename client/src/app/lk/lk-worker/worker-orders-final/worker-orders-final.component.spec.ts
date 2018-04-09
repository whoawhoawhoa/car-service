import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerOrdersFinalComponent } from './worker-orders-final.component';

describe('WorkerOrdersFinalComponent', () => {
  let component: WorkerOrdersFinalComponent;
  let fixture: ComponentFixture<WorkerOrdersFinalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkerOrdersFinalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkerOrdersFinalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
