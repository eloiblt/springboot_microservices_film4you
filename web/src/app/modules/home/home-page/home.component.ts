import { Component, OnInit } from '@angular/core';
import { Film } from '../../../models/film.model';
import { User } from '../../../models/user.model';
import { FilmApiService } from '../../../services/api/film-api.service';
import { RecommandationApiService } from '../../../services/api/recommandation-api.service';
import { UserApiService } from '../../../services/api/user-api.service';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  user: User;
  newestFilms: Film[];
  popularFilms: Film[];
  bestRatedFilms: Film[];
  recommandationFilms: Film[];

  constructor(
    private userApiService: UserApiService,
    private userService: UserService,
    private filmApiService: FilmApiService,
    private recommandationApiService: RecommandationApiService
  ) { }

  ngOnInit() {
    this.userApiService.getCurrentUser().subscribe(res => this.user = res);
    this.filmApiService.getReleasedFilms().subscribe(res => this.newestFilms = res);
    this.filmApiService.getPopularFilms().subscribe(res => this.popularFilms = res);
    this.filmApiService.getMostLovedFilms().subscribe(res => this.bestRatedFilms = res);
    this.recommandationApiService.getRecommandationFilms().subscribe(res => this.recommandationFilms = res);
  }

  logout() {
    this.userService.signout()
  }

}
