import { TestBed, inject } from '@angular/core/testing';

import { AvailableOrderService } from './available-order.service';

describe('AvailableOrderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AvailableOrderService]
    });
  });

  it('should be created', inject([AvailableOrderService], (service: AvailableOrderService) => {
    expect(service).toBeTruthy();
  }));
});
