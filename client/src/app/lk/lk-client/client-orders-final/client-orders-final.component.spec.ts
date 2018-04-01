import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientOrdersFinalComponent } from './client-orders-final.component';

describe('ClientOrdersFinalComponent', () => {
  let component: ClientOrdersFinalComponent;
  let fixture: ComponentFixture<ClientOrdersFinalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientOrdersFinalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientOrdersFinalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
