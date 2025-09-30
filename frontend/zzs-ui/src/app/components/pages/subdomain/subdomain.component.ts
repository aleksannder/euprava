import {Component, OnInit} from '@angular/core';
import {MatCard} from '@angular/material/card';
import {KeyValuePipe, NgForOf, NgIf} from '@angular/common';
import {MatTab, MatTabGroup} from '@angular/material/tabs';
import { ChartModule } from 'primeng/chart';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {SubdomainService} from '../../../services/subdomain.service';
import {DataPointService} from '../../../services/datapoint.service';
import {IndicatorService} from '../../../services/indicator.service';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-subdomain',
  standalone: true,
  imports: [
    MatCard,
    NgIf,
    MatTabGroup,
    MatTab,
    NgForOf,
    ChartModule,
    KeyValuePipe,
    MatButton
  ],
  templateUrl: './subdomain.component.html',
  styleUrl: './subdomain.component.scss'
})
export class SubdomainComponent implements OnInit{
  subdomainCode!: string;
  subdomain: any;
  indicators: any[] = [];
  selectedIndicator: any;
  datapoints: any[] = [];

  chartData: any;
  chartOptions: any;

  constructor(private route: ActivatedRoute,
              private subdomainSvc: SubdomainService,
              private indicatorSvc: IndicatorService,
              private dataPointSvc: DataPointService) {}

  ngOnInit(): void {
      this.subdomainCode = this.route.snapshot.paramMap.get('subdomainCode')!;

      this.subdomainSvc.getSubdomainByCode(this.subdomainCode).subscribe(data => {
        this.subdomain = data;

        this.indicatorSvc.getIndicatorsBySubdomainId(this.subdomain.id).subscribe(indicators => {
          this.indicators = indicators;
        });
      });
  }

  loadData(indicator: any) {
    this.selectedIndicator = indicator;

    this.dataPointSvc.getDatapointsByIndicatorId(indicator.id).subscribe((data: any[]) => {
      this.datapoints = data;

      if (data.length === 0) { return; }

      const dimKeys = Object.keys(data[0].dims);
      const measureKeys = Object.keys(data[0].measures);

      const xKey  = dimKeys[0];
      const yKey = measureKeys[0];

      const labels = data.map(dp => dp.dims[xKey]);
      const values = data.map(dp => dp.measures[yKey]);

      console.info(labels);
      console.info(values);


      this.chartData = {
        labels,
        datasets: [
          {
            label: `${indicator.name} (${yKey})`,
            data: values,
            fill: false,
            borderColor: '#42A5F5'
          }
        ]
      };

      this.chartOptions = {
        responsive: true,
        plugins: {
          legend: {
            display: true,
          }
        }
      };
    });
  }
  protected readonly JSON = JSON;
}
