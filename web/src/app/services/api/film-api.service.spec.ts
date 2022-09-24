import { TestBed } from '@angular/core/testing';

import { FilmApiService } from './film-api.service';

describe('FilmApiService', () => {
  let service: FilmApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FilmApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
