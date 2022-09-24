import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { Film } from '../../models/film.model';
import { Preference } from '../../models/preference.model';
import { Watch } from '../../models/watch.model';
import { GenericApiService } from './generic-api.service';

@Injectable({
  providedIn: 'root'
})
export class PreferencesApiService extends GenericApiService<Film> {

  constructor(
    public httpClient: HttpClient,
    public toastrService: ToastrService
  ) {
    super(httpClient, toastrService);
    this.controllerName = 'preferences/';
  }

  deletePreference(filmId: string) {
    const endPoint = `secured/mark/${filmId}`;
    return this.httpClient.delete(this.apiUrl + this.controllerName + endPoint);
  }

  getFilmUserInfos(filmId: string): Observable<any> {
    const endPoint = `secured/${filmId}/info`;
    return this.httpClient.get<any>(this.apiUrl + this.controllerName + endPoint);
  }

  getCurrentUserPreferences(): Observable<Preference[]> {
    const endPoint = 'secured/user';
    return this.httpClient.get<Preference[]>(this.apiUrl + this.controllerName + endPoint);
  }

  getCurrentUserPreferencesWithFilms(): Observable<Preference[]> {
    const endPoint = 'secured/user/full';
    return this.httpClient.get<Preference[]>(this.apiUrl + this.controllerName + endPoint);
  }

  getPreferenceForFilmIdAndCurrentUser(filmId: string): Observable<Preference> {
    const endPoint = `secured/mark/${filmId}`;
    return this.httpClient.get<Preference>(this.apiUrl + this.controllerName + endPoint);
  }

  addPreference(mark: number, filmId: string) {
    const endPoint = `secured/mark`;
    return this.httpClient.post(this.apiUrl + this.controllerName + endPoint, { filmId, mark });
  }

  updatePreference(mark: number, filmId: string) {
    const endPoint = `secured/mark`;
    return this.httpClient.put(this.apiUrl + this.controllerName + endPoint, { filmId, mark });
  }

  deleteFromWatchlist(filmId: string) {
    const endPoint = `secured/watch/${filmId}`;
    return this.httpClient.delete(this.apiUrl + this.controllerName + endPoint);
  }

  getWatchList(): Observable<Watch[]> {
    const endPoint = `secured/watch`;
    return this.httpClient.get<Watch[]>(this.apiUrl + this.controllerName + endPoint);
  }

  getWatchListFull(): Observable<Watch[]> {
    const endPoint = `secured/watch/full`;
    return this.httpClient.get<Watch[]>(this.apiUrl + this.controllerName + endPoint);
  }

  addIntoWatchList(filmId: string) {
    const endPoint = `secured/watch`;
    return this.httpClient.post(this.apiUrl + this.controllerName + endPoint, { filmId });
  }
}
