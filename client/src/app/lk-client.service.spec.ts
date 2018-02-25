import { TestBed, inject } from '@angular/core/testing';

import { LkClientService } from './lk-client.service';

describe('LkClientService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LkClientService]
    });
  });

  it('should be created', inject([LkClientService], (service: LkClientService) => {
    expect(service).toBeTruthy();
  }));
});
