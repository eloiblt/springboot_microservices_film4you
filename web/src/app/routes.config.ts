import { LoginComponent } from "./modules/login/login.component";
import { HomeComponent } from "./modules/home/home-page/home.component";
import { AuthGuardService } from "./services/auth-guard.service";
import { FilmComponent } from "./modules/home/film/film.component";
import { UserComponent } from "./modules/user/user.component";
import { Routes } from "@angular/router";

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService],
  },
  {
    path: 'login',
    component: LoginComponent,
    pathMatch: 'full',
  },
  {
    path: 'film/:id',
    component: FilmComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService],
    runGuardsAndResolvers: 'always'
  },
  {
    path: 'user/:id',
    component: UserComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService],
  },
  {
    path: 'my-account',
    component: UserComponent,
    pathMatch: 'full',
    canActivate: [AuthGuardService],
  },
];
