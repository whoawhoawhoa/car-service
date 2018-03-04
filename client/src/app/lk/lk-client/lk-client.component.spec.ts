import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LkClientComponent } from './lk-client.component';

describe('LkClientComponent', () => {
  let component: LkClientComponent;
  let fixture: ComponentFixture<LkClientComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LkClientComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LkClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
