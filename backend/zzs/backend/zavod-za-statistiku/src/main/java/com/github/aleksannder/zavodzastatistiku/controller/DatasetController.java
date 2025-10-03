package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.dataset.DatasetRequest;
import com.github.aleksannder.zavodzastatistiku.dto.dataset.DatasetResponse;
import com.github.aleksannder.zavodzastatistiku.service.DatasetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datasets")
@RequiredArgsConstructor
public class DatasetController {

    private final DatasetService datasetService;

    @PostMapping
    public ResponseEntity<DatasetResponse> createDataset(@RequestBody DatasetRequest datasetRequest) {
        return ResponseEntity.ok(datasetService.create(datasetRequest));
    }

    @GetMapping
    public ResponseEntity<List<DatasetResponse>> getAll() {
        return ResponseEntity.ok(datasetService.getAll());
    }

    @GetMapping("/{datasetId}")
    public ResponseEntity<DatasetResponse> getById(@PathVariable Long datasetId) {
        return ResponseEntity.ok(datasetService.getById(datasetId));
    }

    @DeleteMapping("/{datasetId}")
    public ResponseEntity<?> delete(@PathVariable Long datasetId) {
        datasetService.delete(datasetId);
        return ResponseEntity.noContent().build();
    }
}
