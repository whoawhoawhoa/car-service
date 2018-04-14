import { TestBed, inject } from '@angular/core/testing';

import { OrderEsService } from './order-es.service';

describe('OrderEsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrderEsService]
    });
  });

  it('should be created', inject([OrderEsService], (service: OrderEsService) => {
    expect(service).toBeTruthy();
  }));
});
