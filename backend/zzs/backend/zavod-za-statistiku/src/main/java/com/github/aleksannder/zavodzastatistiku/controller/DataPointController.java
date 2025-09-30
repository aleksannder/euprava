package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.ImportJobResponse;
import com.github.aleksannder.zavodzastatistiku.dto.datapoint.DataPointResponse;
import com.github.aleksannder.zavodzastatistiku.model.ImportJob;
import com.github.aleksannder.zavodzastatistiku.service.DataPointService;
import com.github.aleksannder.zavodzastatistiku.service.ImportJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/datapoints")
@RequiredArgsConstructor
public class DataPointController {

    private final DataPointService dataPointService;
    private final ImportJobService importJobService;

    @GetMapping("/datasetVersion/{datasetVersionId}")
    public ResponseEntity<List<DataPointResponse>> getByDatasetVersion(@PathVariable Long datasetVersionId) {
        return ResponseEntity.ok(this.dataPointService.getByDatasetVersion(datasetVersionId));
    }

    @PostMapping("/import/{datasetVersionId}/{indicatorId}")
    public ResponseEntity<ImportJobResponse> importCsv(
            @PathVariable Long datasetVersionId,
            @PathVariable Long indicatorId,
            @RequestParam("file") MultipartFile file
    ) {
        ImportJob job = importJobService.importCsvWithJob(datasetVersionId, indicatorId, file);
        return ResponseEntity.ok(ImportJobResponse.of(job));
    }

    @GetMapping("/export/{datasetVersionId}")
    public ResponseEntity<byte[]> exportCsv(@PathVariable Long datasetVersionId) {
        byte[] csv = importJobService.exportCsv(datasetVersionId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data_points.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    @GetMapping("/indicator/{indicatorId}")
    public ResponseEntity<List<DataPointResponse>> getByIndicatorId(@PathVariable Long indicatorId) {
        return ResponseEntity.ok(dataPointService.getByIndicatorId(indicatorId));
    }
}
