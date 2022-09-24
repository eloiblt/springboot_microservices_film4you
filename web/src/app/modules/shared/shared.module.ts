import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header/header.component';
import { RouterModule } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';
import { HttpClientModule } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { BrowserModule } from '@angular/platform-browser';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { far } from '@fortawesome/free-regular-svg-icons';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { SocialLoginModule } from 'angularx-social-login';
import { SliderFilmsComponent } from './slider-films/slider-films.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ModalComponent } from './modal/modal.component';
import { FormsModule } from '@angular/forms';
import { NgxSpinnerModule } from 'ngx-spinner';
import { TooltipModule } from 'ng2-tooltip-directive';
import { ClickOutsideModule } from 'ng-click-outside';
import { NgxSliderModule } from '@angular-slider/ngx-slider';
import { MiddleclickDirective } from './middle-click.directive';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
  declarations: [
    HeaderComponent,
    SliderFilmsComponent,
    ModalComponent,
    MiddleclickDirective
  ],
  imports: [
    CommonModule,
    RouterModule,
    HttpClientModule,
    ToastrModule,
    FontAwesomeModule,
    BrowserModule,
    TranslateModule,
    SocialLoginModule,
    BrowserAnimationsModule,
    FormsModule,
    NgxSpinnerModule,
    TooltipModule,
    ClickOutsideModule,
    NgxSliderModule,
    NgSelectModule,
    FormsModule
  ],
  exports: [
    CommonModule,
    HeaderComponent,
    RouterModule,
    HttpClientModule,
    ToastrModule,
    FontAwesomeModule,
    BrowserModule,
    TranslateModule,
    SocialLoginModule,
    SliderFilmsComponent,
    BrowserAnimationsModule,
    ModalComponent,
    FormsModule,
    NgxSpinnerModule,
    TooltipModule,
    ClickOutsideModule,
    NgxSliderModule,
    MiddleclickDirective,
    NgSelectModule,
    FormsModule
  ]
})
export class SharedModule {
  constructor(library: FaIconLibrary) {
    library.addIconPacks(fas);
    library.addIconPacks(far);
  }
}
