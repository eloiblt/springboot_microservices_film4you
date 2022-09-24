import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { BehaviorSubject, debounceTime, firstValueFrom } from 'rxjs';
import { UserService } from '../../../services/user.service';
import { UserApiService } from '../../../services/api/user-api.service';
import { User } from '../../../models/user.model';
import { Router } from '@angular/router';
import { FilmApiService } from '../../../services/api/film-api.service';
import { Film } from '../../../models/film.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  searchTerm$ = new BehaviorSubject('');
  user: User;
  showModal = false;
  searchResult: Film[];
  loadedImages = [];
  loading = false;
  langs = ['fr', 'en'];
  advancedSearch = false;
  searchTerm: string;
  genres: string[];
  years: string[];
  selectedGenre: string;
  selectedYear: number;

  @Input()
  theme: string;

  @Output()
  themeChange = new EventEmitter<string>();

  @Input()
  lang: string;

  @Output()
  langChange = new EventEmitter<string>();

  @ViewChild("searchInput") searchInput: ElementRef;

  constructor(
    private userApiService: UserApiService,
    private userService: UserService,
    private router: Router,
    private filmApiService: FilmApiService
  ) { }

  async ngOnInit() {
    this.userApiService.getCurrentUser().subscribe(res => this.user = res);
    this.filmApiService.getAllGenres().subscribe(res => this.genres = res);

    this.searchTerm$.pipe(debounceTime(400)).subscribe({
      next: async (term) => {
        if (!term) {
          this.searchResult = [];
          return;
        }
        this.loading = true;
        this.searchResult = await firstValueFrom(this.filmApiService.searchFilms(term, this.selectedGenre, this.selectedYear));
        this.loading = false;
      }
    });

    this.years = [...Array(new Date().getFullYear() - 1900 + 1).keys()].map(v => (v + 1900).toString()).reverse();
  }

  goToUserPage() {
    this.showModal = false;
    this.router.navigateByUrl(`my-account`);
  }

  logout() {
    this.showModal = false;
    this.userService.signout()
  }

  loaded(id: string) {
    this.loadedImages.push(id);
  }

  isLoaded(id: string) {
    return this.loadedImages.includes(id);
  }

  async goToFilm(id: string) {
    this.searchInput.nativeElement.value = '';
    this.resetSearch();
    await this.router.navigateByUrl('/', { skipLocationChange: true });
    this.router.navigateByUrl(`/film/${id}`);
  }

  goToHomePage() {
    this.resetSearch();
    this.router.navigateByUrl(`/`);
  }

  resetSearch() {
    this.searchResult = [];
    this.searchTerm = '';
    this.selectedGenre = null;
    this.selectedYear = null;
    this.advancedSearch = false;
  }

  changeTheme() {
    if (this.theme === 'light') {
      this.theme = 'dark';
    } else {
      this.theme = 'light';
    }

    this.themeChange.emit(this.theme);
  }

  changeLang() {
    this.langChange.emit(this.lang);
  }

}
