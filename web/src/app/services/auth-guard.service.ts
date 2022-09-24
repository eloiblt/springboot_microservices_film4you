import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {SocialAuthService} from 'angularx-social-login';
import {map, Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(
    private router: Router,
    private socialAuthService: SocialAuthService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.socialAuthService.authState.pipe(
      map(socialUser => {
        if (!socialUser) {
          this.router.navigate(['/login']);
          return false;
        }
        return true;
      })
    );
  }
}
