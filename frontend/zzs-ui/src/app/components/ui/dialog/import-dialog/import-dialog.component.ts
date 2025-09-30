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
    MatOption
  ],
  templateUrl: './import-dialog.component.html',
  styleUrl: './import-dialog.component.scss'
})
export class ImportDialogComponent {
  datasets: any[] = [];
  versions: any[] = [];
  selectedDataset: any;
  selectedVersion: any;
  selectedFile: File | null = null;
  fileName = '';

  constructor(
    private datasetSvc: DatasetService,
    private datasetVersionSvc: DatasetVersionService,
    private importExportSvc: ImportExportService,
    private dialogRef: MatDialogRef<ImportDialogComponent>,
  ) {}

  ngOnInit() {
    this.datasetSvc.getAllDatasets().subscribe(ds => this.datasets = ds);
  }

  loadVersions() {
    if (!this.selectedDataset) return;
    this.datasetVersionSvc.getAllDatasetVersionsByDatasetId(this.selectedDataset.id)
      .subscribe(vs => this.versions = vs);
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.fileName = this.selectedFile?.name ?? '';
  }

  upload() {
    console.info(this.selectedVersion, this.selectedDataset)
    this.importExportSvc.uploadCsv(
      this.selectedVersion.id,
      this.selectedDataset.indicatorId,
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
