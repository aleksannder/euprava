import {Component, OnInit} from '@angular/core';
import {TrafficService} from '../../../services/traffic.service';
import {MatCard} from '@angular/material/card';
import {DecimalPipe, NgIf} from '@angular/common';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow,
  MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {DangerousRegion, FatalitiesTrend, TrafficStat, TrafficSummary} from '../../../model/traffic.model';
import {RegionUtil} from '../../../services/util/region.util';
import {ChartModule} from 'primeng/chart';
import {DataTableComponent} from '../../ui/data-table/data-table.component';
import {Page} from '../../../model/page.model';

@Component({
  selector: 'app-traffic-page',
  standalone: true,
  imports: [
    MatCard,
    NgIf,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCellDef,
    MatCell,
    MatHeaderCellDef,
    MatHeaderRowDef,
    MatHeaderRow,
    MatRowDef,
    MatRow,
    DecimalPipe,
    ChartModule,
    DataTableComponent
  ],
  templateUrl: './traffic-page.component.html',
  styleUrl: './traffic-page.component.scss'
})
export class TrafficPageComponent implements OnInit {
    summaryData: any;
    dangerousRegionsData: any;
    fatalitiesTrendData: any;
    chartOptions: any;
    tableData: TrafficStat[] = [];
    totalItems = 0;
    pageIndex = 0;
    pageSize = 10;
    sort: string = 'year,desc';
    columns = [
      { key: 'year', label: 'Godina'},
      { key: 'region', label: 'Region'},
      { key: 'registeredVehicles', label: 'Registrovana vozila' },
      { key: 'trafficAccidents', label: 'Saobraćajne nesreće' },
      { key: 'fatalities', label: 'Poginuli' }
    ];

    constructor(private trafficService: TrafficService) {}

    ngOnInit(): void {
        this.loadSummary();
        this.loadDangerousRegions();
        this.loadFatalitiesTrend();
        this.loadTable();
        this.chartOptions = { responsive: true, plugins: { legend: { display: true }}};
    }

    loadSummary(): void {
      this.trafficService.getSummary().subscribe((data: TrafficSummary[]) => {
        this.summaryData = {
          labels: data.map(d => d.year),
          datasets: [
            { label: 'Vozila', data: data.map(d => d.totalVehicles), borderColor: '#42A5F5', fill: false },
            { label: 'Nesreće', data: data.map(d => d.totalAccidents), borderColor: '#FF6384', fill: false}
          ]
        };
      });
    }

    loadDangerousRegions(): void {
      this.trafficService.getDangerousRegions(2023).subscribe((data: DangerousRegion[]) => {
        this.dangerousRegionsData = {
          labels: data.map(d => RegionUtil.getLabel(d.region)),
          datasets: [{ label: 'Prosečne nesreće', data: data.map(d => d.avgAccidents), backgroundColor: '#FF7043' }]
        };
      });
    }

    loadFatalitiesTrend(): void {
      this.trafficService.getFatalitiesTrend().subscribe((data: FatalitiesTrend[]) => {
        const regions = [...new Set(data.map(d => d.region))];
        this.fatalitiesTrendData = {
          labels: [...new Set(data.map(d => d.year))],
          datasets: regions.map(region => ({
            label: RegionUtil.getLabel(region),
            data: data.filter(d => d.region === region).map(d => d.fatalities),
            borderColor: this.randomColor(),
            fill: false
          }))
        };
      });
    }

    loadTable(page = 0, size = 10, sort = 'year,desc'): void {
      this.trafficService.getAll(page,size,sort).subscribe((data: Page<TrafficStat>) => {
        this.tableData = data.content;
        this.totalItems = data.totalElements;

      });
    }

    onPageChange(event: any) {
      this.pageIndex = event.pageIndex;
      this.pageSize = event.pageSize;
      this.loadTable(this.pageIndex, this.pageSize, this.sort);
    }

    onSortChange(event: any) {
      console.info(event);
      this.sort = `${event.active},${event.direction}`;
      this.loadTable(this.pageIndex, this.pageSize, this.sort);
    }

    private randomColor() {
      return `#${Math.floor(Math.random()*16777215).toString(16)}`;
    }
}
