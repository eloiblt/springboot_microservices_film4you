import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {Observable} from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GenericApiService<T> {

  protected apiUrl: string;
  protected controllerName = '';

  constructor(
    protected httpClient: HttpClient,
    protected toastrService: ToastrService
  ) {
    this.apiUrl = environment.apiUrl;
  }

  get(): Observable<T[]> {
    return this.httpClient.get<T[]>(this.apiUrl + this.controllerName);
  }

  getById(id: number): Observable<T> {
    const endPoint = '/' + id.toString();
    return this.httpClient.get<T>(this.apiUrl + this.controllerName + endPoint);
  }
}
