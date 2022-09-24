import {TestBed} from '@angular/core/testing';

import {GenericApiService} from './generic-api.service';

describe('GenericApiService', () => {
  let service: GenericApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GenericApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
