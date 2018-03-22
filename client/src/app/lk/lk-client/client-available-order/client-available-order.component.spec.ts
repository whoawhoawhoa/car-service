import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientAvailableOrderComponent } from './client-available-order.component';

describe('ClientAvailableOrderComponent', () => {
  let component: ClientAvailableOrderComponent;
  let fixture: ComponentFixture<ClientAvailableOrderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientAvailableOrderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientAvailableOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
