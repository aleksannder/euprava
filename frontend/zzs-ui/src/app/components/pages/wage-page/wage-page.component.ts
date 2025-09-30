import {Component, OnInit} from '@angular/core';
import {WageGrowth, WageStat} from '../../../model/wage.model';
import {WageService} from '../../../services/wage.service';
import {Region} from '../../../model/enums/region.enum';
import {MatCard} from '@angular/material/card';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow,
  MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {DecimalPipe, JsonPipe, NgIf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {ChartModule} from 'primeng/chart';
import {DataTableComponent} from '../../ui/data-table/data-table.component';
import { RegionUtil } from '../../../services/util/region.util';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-wage-page',
  standalone: true,
  imports: [
    MatCard,
    MatTable,
    MatColumnDef,
    MatHeaderCellDef,
    MatHeaderCell,
    MatCell,
    MatCellDef,
    MatHeaderRowDef,
    MatHeaderRow,
    MatRowDef,
    MatRow,
    JsonPipe,
    DecimalPipe,
    NgIf,
    MatButton,
    ChartModule,
    DataTableComponent,
    MatTooltip
  ],
  templateUrl: './wage-page.component.html',
  styleUrl: './wage-page.component.scss'
})
export class WagePageComponent implements OnInit {
  tableData: WageStat[] = [];
  highlight?: WageGrowth;

  trendChartData: any;
  comparisonChartData: any;
  chartOptions: any;

  constructor(private wageService: WageService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.wageService.getAll().subscribe(data => {
      this.tableData = data;
      this.prepareCharts(data);
    });

    this.wageService.getHighlightByYear(2025).subscribe(data => {
      this.highlight = data;
    });
  }

  prepareCharts(data: WageStat[]): void {
    this.trendChartData = {
      labels: [...new Set(data.map(d => d.year))],
      datasets: [{
        label: 'Prosečna plata (RSD)',
        data: data.map(d => d.averageWage),
        borderColor: '#42A5F5',
        fill: false
      }]
    };

    this.comparisonChartData = {
      labels: [...new Set(data.filter(d => d.year === 2025).map(d => RegionUtil.getLabel(d.region)))],
      datasets: [{
        label: 'Prosečna plata 2025 (RSD)',
        data: data.filter(d => d.year === 2025).map(d => d.averageWage),
        backgroundColor: '#66BB6A'
      }]
    };

    this.chartOptions = {
      responsive: true,
      plugins: { legend: { display: true } }
    };
  }

  protected readonly RegionUtil = RegionUtil;
}
