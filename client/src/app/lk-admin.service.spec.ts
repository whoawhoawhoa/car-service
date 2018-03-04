import { TestBed, inject } from '@angular/core/testing';

import { LkAdminService } from './lk-admin.service';

describe('LkAdminService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LkAdminService]
    });
  });

  it('should be created', inject([LkAdminService], (service: LkAdminService) => {
    expect(service).toBeTruthy();
  }));
});
