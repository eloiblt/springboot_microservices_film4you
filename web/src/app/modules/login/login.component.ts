import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SocialAuthService, GoogleLoginProvider, SocialUser } from 'angularx-social-login';
import { ToastrService } from 'ngx-toastr';
import { firstValueFrom, from } from 'rxjs';
import { UserApiService } from '../../services/api/user-api.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  constructor(
    private router: Router,
    public socialAuthService: SocialAuthService,
    private toastrService: ToastrService,
    private translateService: TranslateService,
    private userApiService: UserApiService
  ) { }

  async loginWithGoogle() {
    try {
      const socialUser = await firstValueFrom(from(this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID)));
      const token = await firstValueFrom(this.userApiService.login(socialUser.idToken));
      localStorage.setItem('token', token);
      this.router.navigateByUrl('');
    } catch (error) {
      this.toastrService.error(this.translateService.instant('login.error'));
    }
  }
}
