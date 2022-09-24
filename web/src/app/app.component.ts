import { Component, ElementRef, OnDestroy, ViewChild } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnDestroy {
  theme = 'dark';
  lang: string;
  scrollBtnVisible = false;
  navigationSubscription: Subscription;

  @ViewChild('containerContent')
  contentContainer: ElementRef;

  get showHeader() {
    return this.router.url !== '/login';
  }

  constructor(
    private translateService: TranslateService,
    private router: Router
  ) {
    const browserLang = this.translateService.getBrowserLang();
    this.lang = browserLang.match(/en|fr/) ? browserLang : 'fr';
    this.setLang(this.lang);

    this.navigationSubscription = this.router.events.subscribe((e: any) => {
      if (e instanceof NavigationEnd) {
        this.scrollToTop();
      }
    });
  }

  setLang(lang: string) {
    this.translateService.use(lang).subscribe({
      next: () => console.info(`Successfully initialized '${lang}' language.`),
      error: () => console.error(`Problem with '${lang}' language initialization.`)
    });
  }

  onScroll() {
    if (this.contentContainer.nativeElement.scrollTop == 0) {
      this.scrollBtnVisible = false;
    } else {
      this.scrollBtnVisible = true;
    }
  }

  scrollToTop() {
    this.contentContainer.nativeElement.scrollTop = 0;
  }

  ngOnDestroy() {
    this.navigationSubscription?.unsubscribe();
  }
}
