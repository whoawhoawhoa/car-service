import { TestBed, inject } from '@angular/core/testing';

import { LkWorkerService } from './lk-worker.service';

describe('LkWorkerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LkWorkerService]
    });
  });

  it('should be created', inject([LkWorkerService], (service: LkWorkerService) => {
    expect(service).toBeTruthy();
  }));
});
