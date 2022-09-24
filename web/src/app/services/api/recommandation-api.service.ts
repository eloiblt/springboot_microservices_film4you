import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { Film } from '../../models/film.model';
import { GenericApiService } from './generic-api.service';

@Injectable({
  providedIn: 'root'
})
export class RecommandationApiService extends GenericApiService<Film> {

  constructor(
    public httpClient: HttpClient,
    public toastrService: ToastrService
  ) {
    super(httpClient, toastrService);
    this.controllerName = 'recommandations/';
  }

  getSimilarFilms(id: string): Observable<Film[]> {
    const endPoint = `secured/lookalike/${id}`;
    return this.httpClient.get<Film[]>(this.apiUrl + this.controllerName + endPoint);
  }

  getRecommandationFilms(): Observable<Film[]> {
    const endPoint = `secured/recommandation`;
    return this.httpClient.get<Film[]>(this.apiUrl + this.controllerName + endPoint);
  }
}
