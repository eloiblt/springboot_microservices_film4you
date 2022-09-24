import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { User } from '../../models/user.model';
import { GenericApiService } from './generic-api.service';

@Injectable({
  providedIn: 'root'
})
export class UserApiService extends GenericApiService<User> {

  constructor(
    public httpClient: HttpClient,
    public toastrService: ToastrService
  ) {
    super(httpClient, toastrService);
    this.controllerName = 'users/';
  }

  deleteUser() {
    const endPoint = 'secured/delete';
    return this.httpClient.delete(this.apiUrl + this.controllerName + endPoint);
  }

  getCurrentUserWithStatistiques() {
    const endPoint = 'secured/summary';
    return this.httpClient.get(this.apiUrl + this.controllerName + endPoint);
  }

  searchUser(searchTerm: string): Observable<User[]> {
    const endPoint = `secured/search?query=${searchTerm}`;
    return this.httpClient.get<User[]>(this.apiUrl + this.controllerName + endPoint);
  }

  getCurrentUserWithPreferences() {
    const endPoint = 'secured/full';
    return this.httpClient.get(this.apiUrl + this.controllerName + endPoint);
  }

  getCurrentUserData() {
    const endPoint = 'secured/data';
    return this.httpClient.get(this.apiUrl + this.controllerName + endPoint);
  }

  getCurrentUser(): Observable<User> {
    const endPoint = 'secured/current';
    return this.httpClient.get<User>(this.apiUrl + this.controllerName + endPoint);
  }

  getStatistiquesByUserId(id: number) {
    const endPoint = `public/${id}/summary`;
    return this.httpClient.get(this.apiUrl + this.controllerName + endPoint);
  }

  getUserByIdWithPreferences(id: number) {
    const endPoint = `public/${id}/full`;
    return this.httpClient.get(this.apiUrl + this.controllerName + endPoint);
  }

  updateVisibility(visibility: string) {
    const endPoint = 'secured/visibility';
    return this.httpClient.post(this.apiUrl + this.controllerName + endPoint, { visibility });
  }

  login(idToken: string) {
    const endPoint = 'public/login';
    return this.httpClient.post(
      this.apiUrl + this.controllerName + endPoint,
      { authType: 'Google', token: idToken },
      { responseType: 'text' }
    );
  }

}
