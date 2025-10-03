package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.dataset.DatasetVersionRequest;
import com.github.aleksannder.zavodzastatistiku.dto.dataset.DatasetVersionResponse;
import com.github.aleksannder.zavodzastatistiku.service.DatasetVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datasetVersions")
@RequiredArgsConstructor
public class DatasetVersionController {

    private final DatasetVersionService datasetVersionService;

    @PostMapping("/{datasetId}")
    public ResponseEntity<DatasetVersionResponse> create(@PathVariable Long datasetId, @RequestBody DatasetVersionRequest req) {
        return ResponseEntity.ok(datasetVersionService.create(datasetId, req));
    }

    @GetMapping("/{datasetVersionId}")
    public ResponseEntity<DatasetVersionResponse> getById(@PathVariable Long datasetVersionId) {
        return ResponseEntity.ok(datasetVersionService.getById(datasetVersionId));
    }

    @GetMapping("/dataset/{datasetId}")
    public ResponseEntity<List<DatasetVersionResponse>> getAllByDataset(@PathVariable Long datasetId) {
        return ResponseEntity.ok(datasetVersionService.getAllByDataset(datasetId));
    }
}
