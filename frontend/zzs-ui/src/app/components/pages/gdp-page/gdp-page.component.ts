import {Component, OnInit} from '@angular/core';
import {GdpService} from '../../../services/gdp.service';
import {GdpStat} from '../../../model/gdp.model';
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
import {GdpGrowth} from '../../../model/gdp-growth.model';
import {Region} from '../../../model/enums/region.enum';
import {MatTooltip} from '@angular/material/tooltip';
import {RegionUtil} from '../../../services/util/region.util';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {MatOption, MatSelect} from '@angular/material/select';
import {ChartModule} from 'primeng/chart';
import {TableModule} from 'primeng/table';
import {DataTableComponent} from '../../ui/data-table/data-table.component';
import {UserInfo} from '../../../model/register-user.model';
import {UsersService} from '../../../services/users/users.service';
import {AuthService} from '@auth0/auth0-angular';
import {take} from 'rxjs';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {ImportExportService} from '../../../services/import-export.service';

@Component({
  selector: 'app-gdp-page',
  standalone: true,
  imports: [
    MatCard,
    JsonPipe,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCellDef,
    MatCell,
    MatHeaderCellDef,
    DecimalPipe,
    MatHeaderRowDef,
    MatHeaderRow,
    MatRowDef,
    MatRow,
    MatTooltip,
    NgIf,
    MatFormField,
    MatLabel,
    FormsModule,
    MatSelect,
    MatOption,
    NgForOf,
    ChartModule,
    TableModule,
    DataTableComponent,
    MatIcon,
    MatIconButton
  ],
  templateUrl: './gdp-page.component.html',
  styleUrl: './gdp-page.component.scss',
  providers: [UsersService],

})
export class GdpPageComponent implements OnInit {
   columns  = [
     { key: 'year', label: 'Godina'},
     { key: 'region', label: 'Region'},
     { key: 'gdpBillion', label: 'BDP (mlrd €)' },
     { key: 'growthPercent', label: 'Rast (%)' }
   ];

   totalItems = 0;
   pageIndex = 0;
   pageSize = 10;
   sortField: string = 'year';
   sortDirection: string = 'desc';

   regions: Region[] = Object.values(Region);
   selectedRegion: string = 'ALL';
   highlight?: GdpGrowth;
   userEmail!: string;

   trendChartData: any;
   regionComparisonData: any;
   chartOptions: any;
   tableData: GdpStat[] = [];

   constructor(private gdpService: GdpService,
               private userService: UsersService,
               private auth: AuthService,
               private csvService: ImportExportService) {
     this.auth.user$.pipe(
       take(1)).subscribe(user => {
       this.userEmail = user?.email as string;
     })
   }

  ngOnInit() {
     this.userService.getUserInfo(this.userEmail).subscribe((userInfo) => {
       this.selectedRegion = userInfo.region;
       this.loadData();
     })
  }

  loadData(pageIndex = 0, pageSize = 10, sort = 'year,desc') {
     if (this.selectedRegion === 'ALL') {
       this.gdpService.getAll(pageIndex, pageSize, sort).subscribe(data => {
         this.prepareCharts(data.content);
         this.tableData = data.content;
         this.totalItems = data.totalElements;
       });
     } else {
       this.gdpService.getByRegion(this.selectedRegion as Region, pageIndex, pageSize, sort).subscribe(data => {
         this.prepareCharts(data.content);
         this.tableData = data.content;
         this.totalItems = data.totalElements;
       });

       this.gdpService.getGrowthPercent(2025, this.selectedRegion as Region)
         .subscribe(data => {
           this.highlight = data;
         })
     }
  }

  prepareCharts(data: GdpStat[]): void {
     this.trendChartData = {
       labels: [...new Set(data.map(d => d.year))],
       datasets: [{
         label: `BDP (mlrd €)`,
         data: data.map(d => d.gdpBillion),
         borderColor: '#42A5F5',
         fill: false,
       }]
     };

    const latestYear = Math.max(...data.map(d => d.year));

    this.regionComparisonData = {
       labels: [...new Set(data.map(d => RegionUtil.getLabel(d.region)))],
       datasets: [{
         label: `BDP ${latestYear}`,
         data: data.filter(d => d.year === latestYear).map(d => d.gdpBillion),
         backgroundColor: '#66BB6A'
       }]
     };

     this.chartOptions = {
       responsive: true,
       plugins: {
         legend: { display: true }
       }
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
    this.csvService.exportCsv('GDP').subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'gdp-data.csv';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    })
  }

  protected readonly RegionUtil = RegionUtil;
  protected readonly Region = Region;
}
