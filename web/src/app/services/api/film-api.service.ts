import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { Film } from '../../models/film.model';
import { GenericApiService } from './generic-api.service';

@Injectable({
  providedIn: 'root'
})
export class FilmApiService extends GenericApiService<Film> {

  constructor(
    public httpClient: HttpClient,
    public toastrService: ToastrService
  ) {
    super(httpClient, toastrService);
    this.controllerName = 'films/';
  }

  getFilmById(id: string): Observable<Film> {
    const endPoint = `secured/${id}`;
    return this.httpClient.get<Film>(this.apiUrl + this.controllerName + endPoint);
  }

  searchFilms(searchTerm: string, genre: string = '', year: number = 0): Observable<Film[]> {
    const endPoint = `secured/search?query=${searchTerm}`;
    let body = {};
    if (genre && year) { body = { genre, year }}
    else if (genre) { body = { genre }}
    else if (year) { body = { year }}
    return this.httpClient.post<Film[]>(this.apiUrl + this.controllerName + endPoint, body);
  }

  getReleasedFilms(): Observable<Film[]> {
    const endPoint = 'secured/released';
    return this.httpClient.get<Film[]>(this.apiUrl + this.controllerName + endPoint);
  }

  getPopularFilms(): Observable<Film[]> {
    const endPoint = 'secured/popular';
    return this.httpClient.get<Film[]>(this.apiUrl + this.controllerName + endPoint);
  }

  getMostLovedFilms(): Observable<Film[]> {
    const endPoint = 'secured/most/loved';
    return this.httpClient.get<Film[]>(this.apiUrl + this.controllerName + endPoint);
  }

  getAllGenres(): Observable<string[]> {
    const endPoint = 'secured/genres';
    return this.httpClient.get<string[]>(this.apiUrl + this.controllerName + endPoint);
  }
}
