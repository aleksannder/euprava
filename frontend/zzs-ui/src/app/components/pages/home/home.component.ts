import {Component, OnInit} from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {AsyncPipe, DecimalPipe, NgForOf, NgIf} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {DomainService} from '../../../services/domain/domain.service';
import {MatIcon} from '@angular/material/icon';
import {DashboardStats, StatsService} from '../../../services/stats.service';
import {CardComponent} from '../../ui/cards/highlight-card/card.component';
import {CitizenDashboard} from '../../../model/citizen-dashboard.model';
import {UsersService} from '../../../services/users/users.service';
import {RegionUtil} from '../../../services/util/region.util';
import {AuthTokenUtil} from '../../../services/auth/auth-token.util';
import {Observable} from 'rxjs';
import {MatTooltip} from '@angular/material/tooltip';

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
    NgIf,
    AsyncPipe,
    MatTooltip,
    RouterLink
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  providers: [UsersService],
})
export class HomeComponent implements OnInit {

  statisticsHighlights!: DashboardStats;
  mupHighlights!: any[];
  statHighlights!: any[];

  constructor(private statsService: StatsService, private auth: AuthTokenUtil ) {}

  ngOnInit() {
    this.auth.getUserProfile().subscribe((user) => {
      this.statsService.getHighlights(user.email).subscribe((highlights) => {
        this.statisticsHighlights = highlights;
        this.loadHighlights();
      })

    });
  }

  loadHighlights(): void {
      this.mupHighlights = [
        { label: 'Izdate lične karte', value: this.statisticsHighlights.identificationCards, icon: 'attribution', description: 'Broj odobrenih zahteva za ličnu kartu' },
        { label: 'Dozvole za oružje', value: this.statisticsHighlights.gunPermits, icon: 'security', description: 'Broj izdatih dozvola za oružje' },
        { label: 'Vozačke dozvole', value: this.statisticsHighlights.driversLicenses, icon: 'category', description: 'Broj izdatih vozačkih dozvola'},
        { label: 'Saobraćajne dozvole', value: this.statisticsHighlights.vehicleLicences, icon: 'directions_car', description: 'Broj izdatih saobraćajnih dozvola'},
      ];

      this.statHighlights = [
        { label: 'BDP rast', value: this.statisticsHighlights.gdpGrowth.growthPercent.toFixed(2) + '%', icon: 'trending_up', description: 'Rast BDP u tekućoj godini', link: 'gdp' },
        { label: 'Najveći porast plata', value: this.statisticsHighlights.highestPaidRegion.growthRsd + ' RSD', icon: 'payments', description: 'Region sa najvećim rastom plata', link: 'wage' },
        { label: 'Najmlađi region', value: RegionUtil.getLabel(this.statisticsHighlights.extremesDto.youngestRegion), icon: 'child_care', description: 'Region sa najmanjom prosečnom starošću', link: 'population' }
      ];
  }
  protected readonly RegionUtil = RegionUtil;
}
