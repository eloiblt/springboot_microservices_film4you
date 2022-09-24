import { Component, ElementRef, Input, OnChanges, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { faDraftingCompass } from '@fortawesome/free-solid-svg-icons';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { Film } from '../../../models/film.model';

@Component({
  selector: 'app-slider-films',
  templateUrl: './slider-films.component.html',
  styleUrls: ['./slider-films.component.scss']
})
export class SliderFilmsComponent implements OnInit, OnChanges {

  @Input()
  films: Film[];

  @Input()
  title: string;

  @Input()
  noDataText: string;

  @ViewChild('scrollableSlider')
  scrollableSlider: ElementRef;

  printedFilms: Film[];
  numberOfFilms = 0;
  currentIndex = 0;

  loadedImagesIds = [];

  constructor(
    private spinner: NgxSpinnerService,
    private router: Router,
    private translateService: TranslateService
  ) {}

  ngAfterViewInit() {
    this.numberOfFilms = Math.floor(this.scrollableSlider?.nativeElement?.offsetWidth / 220);
  }

  ngOnInit(): void {
    this.spinner.show();
  }

  ngOnChanges() {
    setTimeout(() => this.printedFilms = this.films?.slice(0, this.numberOfFilms), 0);
  }

  public goRight(): void {
    if (this.films?.length < this.numberOfFilms) { return; }
    this.currentIndex++;
    if (this.currentIndex === this.films.length) {
      this.currentIndex = 0;
    }

    this.printedFilms = this.films.slice(this.currentIndex).slice(0, this.numberOfFilms);
    if (this.printedFilms.length < this.films.length) {
      this.printedFilms = [...this.printedFilms, ...this.films.slice(0, this.numberOfFilms - this.printedFilms.length)];
    }

    this.loadedImagesIds = this.printedFilms.map(f => f.id);
    this.loadedImagesIds.pop();
  }

  public goLeft(): void {
    if (this.films?.length < this.numberOfFilms) { return; }
    this.currentIndex--;
    if (this.currentIndex === -1) {
      this.currentIndex = this.films.length - 1;
    }

    this.printedFilms = this.films.slice(this.currentIndex).slice(0, this.numberOfFilms);
    if (this.printedFilms.length < this.films.length) {
      this.printedFilms = [...this.printedFilms, ...this.films.slice(0, this.numberOfFilms - this.printedFilms.length)]
    }

    this.loadedImagesIds = this.printedFilms.map(f => f.id);
    this.loadedImagesIds.shift();
  }

  async goToFilm(id: string) {
    await this.router.navigateByUrl(`/`, { skipLocationChange: true });
    this.router.navigateByUrl(`/film/${id}`);
  }

  goToFilmNewTab(id: string) {
    window.open(`/film/${id}`, '_blank');
  }

  loaded(id: string) {
    this.loadedImagesIds.push(id);
  }

  isLoaded(id: string) {
    return this.loadedImagesIds.includes(id);
  }

  getNoDataText() {
    return this.noDataText ?? this.translateService.instant('slider.noFilms');
  }
}
