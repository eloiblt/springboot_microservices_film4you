import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { SocialAuthService } from 'angularx-social-login';
import { User } from '../models/user.model';
import { UserApiService } from './api/user-api.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    public socialAuthService: SocialAuthService,
    private router: Router,
    private userApiService: UserApiService
  ) { }

  getToken() {
    return localStorage.getItem('token');
  }

  signout() {
    localStorage.removeItem('token');
    this.socialAuthService.signOut();
    this.router.navigateByUrl('login');
  }
}
