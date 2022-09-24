
import { AppComponent } from './app.component';
import { HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpInterceptorService } from './services/http-interceptor.service';
import { HomeModule } from './modules/home/home.module';
import { NgModule } from '@angular/core';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { GoogleLoginProvider } from 'angularx-social-login';
import { SharedModule } from './modules/shared/shared.module';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { routes } from './routes.config';
import { ToastrModule } from 'ngx-toastr';
import { UserModule } from './modules/user/user.module';
import { LoginModule } from './modules/login/login.module';
import { TooltipModule, TooltipOptions } from 'ng2-tooltip-directive';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient);
}

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    SharedModule,
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
      maxOpened: 1
    }),
    RouterModule.forRoot(routes, {
      initialNavigation: 'enabled',
      anchorScrolling: 'enabled',
      onSameUrlNavigation: 'reload',
      scrollPositionRestoration: 'top'
    }),
    BrowserModule.withServerTransition({ appId: 'ng-cli-universal' }),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    TooltipModule.forRoot({ placement: 'bottom', hideDelay: 0 } as TooltipOptions),
    HomeModule,
    LoginModule,
    UserModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorService, multi: true },
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: true,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider('289089852412-vub85om86eqi72obavfft3amnm7f47ga.apps.googleusercontent.com')
          }
        ]
      }
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
