import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerAvailableOrdersComponent } from './worker-available-orders.component';

describe('WorkerAvailableOrdersComponent', () => {
  let component: WorkerAvailableOrdersComponent;
  let fixture: ComponentFixture<WorkerAvailableOrdersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkerAvailableOrdersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkerAvailableOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
