import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerAuthComponent } from './worker-auth.component';

describe('WorkerAuthComponent', () => {
  let component: WorkerAuthComponent;
  let fixture: ComponentFixture<WorkerAuthComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkerAuthComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkerAuthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
