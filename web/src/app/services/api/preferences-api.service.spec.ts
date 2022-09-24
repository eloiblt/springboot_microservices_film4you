import { TestBed } from '@angular/core/testing';

import { PreferencesApiService } from './preferences-api.service';

describe('PreferencesApiService', () => {
  let service: PreferencesApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PreferencesApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
