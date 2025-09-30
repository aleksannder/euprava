import {Component, OnInit} from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {DecimalPipe, NgForOf, NgIf} from '@angular/common';
import {Router} from '@angular/router';
import {DomainService} from '../../../services/domain/domain.service';
import {MatIcon} from '@angular/material/icon';
import {StatsService} from '../../../services/stats.service';
import {CardComponent} from '../../ui/cards/highlight-card/card.component';
import {CitizenDashboard} from '../../../model/citizen-dashboard.model';
import {UsersService} from '../../../services/users/users.service';
import {RegionUtil} from '../../../services/util/region.util';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    MatCard,
    NgForOf,
    MatIcon,
    CardComponent,
    MatCardTitle,
    MatCardHeader,
    MatCardContent,
    DecimalPipe,
    NgIf
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  providers: [UsersService],
})
export class HomeComponent implements OnInit {
    dashboard?: CitizenDashboard;

    constructor(private userService: UsersService) {}

    ngOnInit(): void {
        this.userService.getUserDashboardInfo().subscribe(data => {
          this.dashboard = data;
        })
    }

  protected readonly RegionUtil = RegionUtil;
}
