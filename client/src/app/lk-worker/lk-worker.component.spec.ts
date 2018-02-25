import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LkWorkerComponent } from './lk-worker.component';

describe('LkWorkerComponent', () => {
  let component: LkWorkerComponent;
  let fixture: ComponentFixture<LkWorkerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LkWorkerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LkWorkerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
