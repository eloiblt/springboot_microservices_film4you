import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SliderFilmsComponent } from './slider-films.component';

describe('SliderFilmsComponent', () => {
  let component: SliderFilmsComponent;
  let fixture: ComponentFixture<SliderFilmsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SliderFilmsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SliderFilmsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
