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
import {MatButton, MatIconButton} from '@angular/material/button';
import {ChartModule} from 'primeng/chart';
import {DataTableComponent} from '../../ui/data-table/data-table.component';
import { RegionUtil } from '../../../services/util/region.util';
import {MatTooltip} from '@angular/material/tooltip';
import {MatIcon} from '@angular/material/icon';
import {ImportExportService} from '../../../services/import-export.service';

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
    MatTooltip,
    MatIconButton,
    MatIcon
  ],
  templateUrl: './wage-page.component.html',
  styleUrl: './wage-page.component.scss'
})
export class WagePageComponent implements OnInit {
  tableData: WageStat[] = [];
  highlight?: WageGrowth;

  totalItems = 0;
  pageIndex = 0;
  pageSize = 10;
  sortField: string = 'year';
  sortDirection: string = 'desc';

  trendChartData: any;
  comparisonChartData: any;
  chartOptions: any;

  constructor(private wageService: WageService, private csvService: ImportExportService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(pageIndex = 0, pageSize = 10, sort = 'year,desc'): void {
    this.wageService.getAll(pageIndex, pageSize, sort).subscribe(data => {
      this.tableData = data.content;
      this.prepareCharts(data.content);
      this.totalItems = data.totalElements;
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

    const latestYear = Math.max(...data.map(d => d.year));

    this.comparisonChartData = {
      labels: [...new Set(data.filter(d => d.year === latestYear).map(d => RegionUtil.getLabel(d.region)))],
      datasets: [{
        label: `Prosečna plata ${latestYear} (RSD)`,
        data: data.filter(d => d.year === latestYear).map(d => d.averageWage),
        backgroundColor: '#66BB6A'
      }]
    };

    this.chartOptions = {
      responsive: true,
      plugins: { legend: { display: true } }
    };
  }

  onPageChange(event: any) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData(this.pageIndex, this.pageSize);
  }

  onSortChange(event: any) {
    this.sortField = event.active;
    this.sortDirection = event.direction;
    this.loadData(0, 10, `${this.sortField},${this.sortDirection}`);
  }

  exportCsv() {
    this.csvService.exportCsv('WAGE').subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'wage-data.csv';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    })
  }
  protected readonly RegionUtil = RegionUtil;
}
