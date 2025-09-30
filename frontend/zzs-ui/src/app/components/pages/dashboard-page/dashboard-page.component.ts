import { Component } from '@angular/core';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {MatListItem, MatNavList} from '@angular/material/list';
import {RouterLink, RouterOutlet} from '@angular/router';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {FooterComponent} from '../../ui/footer/footer.component';
import {NgIf} from '@angular/common';
import {MatDialog} from '@angular/material/dialog';
import {ImportDialogComponent} from '../../ui/dialog/import-dialog/import-dialog.component';
import {SurveyDialogComponent} from '../../ui/dialog/survey-dialog/survey-dialog.component';
import {SurveyService} from '../../../services/survey.service';

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
    NgIf
  ],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent {
  isExpanded = true;

  constructor(private dialog: MatDialog, private surveyService: SurveyService) {}

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
}
