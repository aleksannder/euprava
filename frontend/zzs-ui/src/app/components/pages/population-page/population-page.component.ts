import {Component, OnInit} from '@angular/core';
import {PopulationService} from '../../../services/population.service';
import {Region} from '../../../model/enums/region.enum';
import {AvgAgeData, PopulationStat} from '../../../model/population.model';
import {Observable} from 'rxjs';
import {MatButton} from '@angular/material/button';
import {MatCard} from '@angular/material/card';
import {DecimalPipe, JsonPipe, NgForOf, NgIf} from '@angular/common';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow,
  MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {RegionUtil} from '../../../services/util/region.util';
import {MatFormField, MatOption, MatSelect} from '@angular/material/select';
import {FormsModule} from '@angular/forms';
import {ChartModule} from 'primeng/chart';
import {MatTooltip} from '@angular/material/tooltip';
import {MatLabel} from '@angular/material/form-field';

@Component({
  selector: 'app-population-page',
  standalone: true,
  imports: [
    MatButton,
    MatCard,
    NgIf,
    JsonPipe,
    MatTable,
    MatColumnDef,
    MatHeaderCellDef,
    MatHeaderCell,
    MatCell,
    MatCellDef,
    DecimalPipe,
    MatHeaderRowDef,
    MatHeaderRow,
    MatRowDef,
    MatRow,
    NgForOf,
    MatSelect,
    MatLabel,
    MatOption,
    FormsModule,
    MatFormField,
    ChartModule,
    MatTooltip
  ],
  templateUrl: './population-page.component.html',
  styleUrl: './population-page.component.scss'
})
export class PopulationPageComponent implements OnInit {
  regions: Region[] = Object.values(Region);
  selectedRegion: string = 'RS11_BELGRADE';

  trendData: any;
  shareData: any;
  chartOptions: any;

  avgAgeData: any;
  naturalGrowthData: any;

  highlightExtremes: any;
  projectionData: any;

  constructor(private populationService: PopulationService) {}

    ngOnInit(): void {
        this.loadData();
    }

    loadData(): void {
    this.populationService.getPopulationTrendForRegion(this.selectedRegion as Region).subscribe(data => {
      this.trendData = {
        labels: data.map(d => d.year),
        datasets: [
          {
            label: 'Populacija',
            data: data.map(d => d.population),
            borderColor: '#42A5F5',
            fill: false
          }
        ]
      };
    });

    this.populationService.getAvgAge().subscribe(data => {
      this.avgAgeData = {
        labels: data.map(d => RegionUtil.getLabel(d.region)),
        datasets: [
          { label: 'Prosečna starost', data: data.map(d => d.value), backgroundColor: '#66BB6A'}
        ]
      };
    });

    this.populationService.getNaturalGrowth().subscribe(data => {
      this.naturalGrowthData = {
        labels: data.map(d => RegionUtil.getLabel(d.region)),
        datasets: [
          { label: 'Prirodni priraštaj (%)', data: data.map(d => d.value), backgroundColor: '#FFA726'}
        ]
      };
    });

    this.populationService.getExtremes().subscribe(data => {
      this.highlightExtremes = data;
    });

    this.populationService.getProjection(this.selectedRegion as Region).subscribe(data => {
      this.projectionData = {
        labels: data.map(d => d.year),
        datasets: [
          { label: 'Projekcija populacije', data: data.map(d => d.projectedPopulation), borderColor: '#AB47BC', fill: false }
        ]
      };
    });

    this.populationService.getPopulationShare(2025).subscribe(data => {
      this.shareData = {
        labels:  [...new Set(data.map(d => RegionUtil.getLabel(d.region)))],
        datasets: [
          { data: data.map(d => d.value), backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726', '#EF5350']}
        ]
      };
    });

    this.chartOptions = { responsive: true, plugins: { legend: { display: true } } };
    }

  protected readonly RegionUtil = RegionUtil;
}
