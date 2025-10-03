import {Component, EventEmitter, Input, Output} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow,
  MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {MatSort, MatSortHeader, Sort} from '@angular/material/sort';
import {NgForOf} from '@angular/common';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {RegionUtil} from '../../../services/util/region.util';

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [
    MatTable,
    MatSort,
    MatColumnDef,
    MatHeaderCellDef,
    MatHeaderCell,
    MatCellDef,
    MatCell,
    NgForOf,
    MatHeaderRowDef,
    MatHeaderRow,
    MatRowDef,
    MatSortHeader,
    MatRow,
    MatPaginator
  ],
  templateUrl: './data-table.component.html',
  styleUrl: './data-table.component.scss'
})
export class DataTableComponent<T> {
    @Input() columns: { key: string; label: string}[] = [];
    @Input() data: T[] = [];
    @Input() totalItems = 0;
    @Input() pageSize = 10;
    @Input() pageIndex = 0;

    @Output() pageChange = new EventEmitter<PageEvent>();
    @Output() sortChange = new EventEmitter<Sort>();

    protected readonly RegionUtil = RegionUtil;

    get displayedColumns(): string[] {
      return this.columns.map(c => c.key);
    }

    onPageChange(event: PageEvent) {
      this.pageChange.emit(event);
    }

    onSortChange(event: Sort) {
      this.sortChange.emit(event);
    }

}
