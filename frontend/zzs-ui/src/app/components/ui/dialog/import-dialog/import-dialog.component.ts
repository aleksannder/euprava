import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {NgForOf, NgIf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {ImportExportService} from '../../../../services/import-export.service';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatOption, MatSelect} from '@angular/material/select';
import {FormsModule} from '@angular/forms';
import {DatasetService} from '../../../../services/dataset.service';
import {DatasetVersionService} from '../../../../services/datasetVersion.service';
import {Region} from '../../../../model/enums/region.enum';
import {Domains} from '../../../../model/enums/domains.enum';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-import-dialog',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatDialogContent,
    NgIf,
    MatDialogActions,
    MatLabel,
    MatButton,
    MatFormField,
    MatSelect,
    FormsModule,
    NgForOf,
    MatIcon,
    MatOption
  ],
  templateUrl: './import-dialog.component.html',
  styleUrl: './import-dialog.component.scss'
})
export class ImportDialogComponent {
  domains = [
    { value: 'GDP', label: 'BDP' },
    { value: 'TRAFFIC', label: 'Saobraćaj' },
    { value: 'POP', label: 'Građani' },
    { value: 'WAGE', label: 'Plate' }
  ];

  selectedDomain!: string;
  selectedFile: File | null = null;
  fileName = '';

  constructor(
    private importExportSvc: ImportExportService,
    private dialogRef: MatDialogRef<ImportDialogComponent>,
  ) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.fileName = this.selectedFile?.name ?? '';
  }

  upload() {
    this.importExportSvc.uploadCsv(
      this.selectedDomain,
      this.selectedFile!
    ).subscribe({
      next: (res) => this.dialogRef.close(res),
      error: (err) => alert('Upload failed: ' + err.message)
    });
  }

  close() {
    this.dialogRef.close();
  }
}
