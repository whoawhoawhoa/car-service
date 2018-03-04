import { TestBed, inject } from '@angular/core/testing';

import { CarTypeService } from './car-type.service';

describe('CarTypeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CarTypeService]
    });
  });

  it('should be created', inject([CarTypeService], (service: CarTypeService) => {
    expect(service).toBeTruthy();
  }));
});
