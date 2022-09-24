import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, debounceTime, firstValueFrom } from 'rxjs';
import { Film } from '../../models/film.model';
import { User } from '../../models/user.model';
import { PreferencesApiService } from '../../services/api/preferences-api.service';
import { UserApiService } from '../../services/api/user-api.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  searchTerm$ = new BehaviorSubject('');
  user: User;
  watchList: Film[];
  preferences: Film[];
  searchResult = [];
  searchLoading = false;
  isCurrentUser = false;

  get loading() {
    if (!this.isCurrentUser) { return !this.user; }
    return !(this.user && this.watchList && this.preferences);
  }

  @ViewChild("searchInput") searchInput: ElementRef

  constructor(
    private userApiService: UserApiService,
    private preferencesApiService: PreferencesApiService,
    private toastrService: ToastrService,
    private userService: UserService,
    private spinner: NgxSpinnerService,
    private route: ActivatedRoute,
    private router: Router,
    private translateService: TranslateService
  ) { }

  async ngOnInit() {
    if (this.router.url === '/my-account') {
      this.isCurrentUser = true;

      this.userApiService.getCurrentUserWithStatistiques().subscribe(res => this.user = res);
      this.preferencesApiService.getWatchListFull().subscribe(res => this.watchList = res.map(w => w.film));
      this.preferencesApiService.getCurrentUserPreferencesWithFilms().subscribe(res => {
        this.preferences = res.map(w => { return { ...w.film, userNote: w.mark }});
      });
    } else {
      const currentUser = await firstValueFrom(this.userApiService.getCurrentUser());

      const id = parseInt(this.route.snapshot.paramMap.get('id'));
      if (currentUser.id === id) {
        this.router.navigateByUrl(`/my-account`);
      }

      this.userApiService.getStatistiquesByUserId(id).subscribe({
        next: (res) => this.user = res,
        error: () => {
          this.toastrService.error('User non trouvé');
          this.router.navigateByUrl('/');
        }
      });
    }

    this.spinner.show();

    this.searchTerm$.pipe(debounceTime(400)).subscribe({
      next: async (term) => {
        if (!term) {
          this.searchResult = [];
          return;
        }

        this.searchLoading = true;
        this.searchResult = await firstValueFrom(this.userApiService.searchUser(term));
        this.searchLoading = false;
      }
    });
  }

  async downloadData() {
    const data = await firstValueFrom(this.userApiService.getCurrentUserData());

    var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(data));
    var dlAnchorElem = document.createElement('a');
    dlAnchorElem.setAttribute("href", dataStr);
    dlAnchorElem.setAttribute("download", "my-data.json");
    dlAnchorElem.click();
  }

  close() {
    this.searchResult = [];
    this.searchInput.nativeElement.value = '';
  }

  async goToUser(id: number) {
    this.close();
    await this.router.navigateByUrl(`/`, { skipLocationChange: true });
    this.router.navigateByUrl(`/user/${id}`);
  }

  async updateVisibility() {
    await firstValueFrom(this.userApiService.updateVisibility(this.user.visibility));
    this.toastrService.success('Visibilité changée');
  }

  async deleteAccount() {
    await firstValueFrom(this.userApiService.deleteUser());
    this.userService.signout();
  }

  formatUnknownValue(value: string) {
    return value === 'Unknown' ? this.translateService.instant('user.stats.noData') : value;
  }
}
