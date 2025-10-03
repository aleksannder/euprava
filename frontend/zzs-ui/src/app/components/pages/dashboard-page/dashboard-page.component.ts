import {Component, OnInit} from '@angular/core';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {MatListItem, MatNavList} from '@angular/material/list';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {FooterComponent} from '../../ui/footer/footer.component';
import {AsyncPipe, NgIf} from '@angular/common';
import {MatDialog} from '@angular/material/dialog';
import {ImportDialogComponent} from '../../ui/dialog/import-dialog/import-dialog.component';
import {SurveyDialogComponent} from '../../ui/dialog/survey-dialog/survey-dialog.component';
import {SurveyService} from '../../../services/survey.service';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {AuthService} from '@auth0/auth0-angular';
import {map, Observable, pipe} from 'rxjs';
import {AuthTokenUtil} from '../../../services/auth/auth-token.util';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [
    MatSidenavContainer,
    MatSidenav,
    MatSidenavContent,
    MatNavList,
    MatListItem,
    RouterLink,
    MatToolbar,
    MatIconButton,
    MatIcon,
    RouterOutlet,
    FooterComponent,
    NgIf,
    MatMenuTrigger,
    AsyncPipe,
    MatMenu,
    MatMenuItem
  ],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent implements OnInit {
  isExpanded = true;
  isAuthenticated$!: Observable<boolean>;
  isAnalyst$!: Observable<boolean>;
  isCitizen$!: Observable<boolean>;

  constructor(private dialog: MatDialog,
              private surveyService: SurveyService,
              private auth: AuthService,
              private authUtil: AuthTokenUtil,
              private router: Router) {
    this.isAuthenticated$ = this.auth.isAuthenticated$;
    this.isAnalyst$ = this.authUtil.hasPermission('zzs:analyst');
    this.isCitizen$ = this.authUtil.hasPermission('citizen:access');
  }

  ngOnInit(): void {

    }

  toggleSidebar() {
    this.isExpanded = !this.isExpanded;
  }

  onImport() {
    this.dialog.open(ImportDialogComponent, {
      width: '500px',
    })
  }

  onCreateSurvey() {
    this.dialog.open(SurveyDialogComponent);
  }

  goToSettings() {
    this.router.navigate(['/settings']);
  }

  logout(): void {
    this.auth.logout({ logoutParams: {
      returnTo: window.location.origin
      }});
  }
}
