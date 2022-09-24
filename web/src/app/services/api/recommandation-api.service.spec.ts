import { TestBed } from '@angular/core/testing';

import { RecommandationApiService } from './recommandation-api.service';

describe('RecommandationApiService', () => {
  let service: RecommandationApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecommandationApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
