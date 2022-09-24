import { Options } from '@angular-slider/ngx-slider';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { firstValueFrom, Subscription } from 'rxjs';
import { Film } from '../../../models/film.model';
import { FilmApiService } from '../../../services/api/film-api.service';
import { PreferencesApiService } from '../../../services/api/preferences-api.service';
import { RecommandationApiService } from '../../../services/api/recommandation-api.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-film',
  templateUrl: './film.component.html',
  styleUrls: ['./film.component.scss']
})
export class FilmComponent implements OnInit, OnDestroy {

  film: Film;
  showMarkModal = false;
  mark: number;
  tempMark: number;
  loaded = false;
  options: Options = {
    floor: 0,
    ceil: 10,
    step: 0.1
  };
  isIntoWatchList = false;
  similarFilms: Film[];
  navigationSubscription: Subscription;

  constructor(
    private filmApiService: FilmApiService,
    private route: ActivatedRoute,
    private preferencesApiService: PreferencesApiService,
    private toastrService: ToastrService,
    private router: Router,
    private recommandationApiService: RecommandationApiService,
    private location: Location
  ) {
    this.navigationSubscription = this.router.events.subscribe((e: any) => {
      if (e instanceof NavigationEnd) {
        this.film = null;
        this.ngOnInit();
      }
    });
  }

  async ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');

    this.filmApiService.getFilmById(id).subscribe({
      next: res => this.film = res,
      error: () => this.router.navigateByUrl('')
    });

    this.preferencesApiService.getFilmUserInfos(id).subscribe({
      next: infos => {
        this.mark = infos.mark;
        this.isIntoWatchList = infos.isInWatchlist;
        this.tempMark = this.mark;
      },
      error: () => this.mark = null
    });

    this.recommandationApiService.getSimilarFilms(id).subscribe(res => this.similarFilms = res);
  }

  async addOrUpdateMark() {
    if (this.mark) {
      await firstValueFrom(this.preferencesApiService.updatePreference(this.tempMark, this.film.id));
      this.toastrService.success('Note modifiée');
    } else {
      await firstValueFrom(this.preferencesApiService.addPreference(this.tempMark, this.film.id));
      this.toastrService.success('Note ajoutée');
    }
    this.mark = this.tempMark;
    this.showMarkModal = false;
  }

  async deleteMark() {
    await firstValueFrom(this.preferencesApiService.deletePreference(this.film.id));
    this.toastrService.success('Note supprimée');
    this.showMarkModal = false;
    this.tempMark = null;
    this.mark = null;
  }

  async toggleWatchList() {
    if (this.isIntoWatchList) {
      await firstValueFrom(this.preferencesApiService.deleteFromWatchlist(this.film.id));
      this.toastrService.success('Film supprimé votre watchlist');
    } else {
      await firstValueFrom(this.preferencesApiService.addIntoWatchList(this.film.id));
      this.toastrService.success('Film ajouté à votre watchlist');
    }

    this.isIntoWatchList = !this.isIntoWatchList;
  }

  goBackBtn() {
    this.location.back();
  }

  ngOnDestroy() {
    this.navigationSubscription?.unsubscribe();
  }
}
