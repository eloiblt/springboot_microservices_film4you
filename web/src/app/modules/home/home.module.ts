import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { HomeComponent } from './home-page/home.component';
import { FilmComponent } from './film/film.component';

@NgModule({
  declarations: [
    HomeComponent,
    FilmComponent
  ],
  imports: [
    SharedModule
  ]
})
export class HomeModule {
}
