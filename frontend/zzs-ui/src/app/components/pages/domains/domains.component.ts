import {Component, OnInit} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {DomainService} from '../../../services/domain/domain.service';
import {NgForOf, NgIf} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {CardComponent} from '../../ui/cards/highlight-card/card.component';
import {DomainCardComponent} from '../../ui/cards/domain-card/domain-card.component';

@Component({
  selector: 'app-domains',
  standalone: true,
  imports: [
    MatTable,
    MatColumnDef,
    MatHeaderCellDef,
    MatHeaderCell,
    MatCell,
    MatCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRowDef,
    MatRow,
    NgIf,
    CardComponent,
    NgForOf,
    DomainCardComponent
  ],
  templateUrl: './domains.component.html',
  styleUrl: './domains.component.scss'
})
export class DomainsComponent implements OnInit {
  domains: any[] = [];
  displayedColumns: string[] = ['id', 'code', 'name'];
  allDomains: boolean = true;
  domainCode?: string;

  constructor(private domainService: DomainService,
              private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.domainCode = params.get('domainCode') || undefined;
      if (this.domainCode) {
        // this.domainService.getDomainWithCode(this.domainCode).subscribe(data => {
        //
        // })
      } else {
        this.domainService.getAll().subscribe(data => {
          this.domains = data;
        });
      }
    });
  }

}
