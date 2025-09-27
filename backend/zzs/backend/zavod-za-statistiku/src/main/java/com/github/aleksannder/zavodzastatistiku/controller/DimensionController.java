package com.github.aleksannder.zavodzastatistiku.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aleksannder.zavodzastatistiku.dto.dimension.DimensionRequest;
import com.github.aleksannder.zavodzastatistiku.dto.dimension.DimensionResponse;
import com.github.aleksannder.zavodzastatistiku.model.Dimension;
import com.github.aleksannder.zavodzastatistiku.service.DimensionService;
import com.github.aleksannder.zavodzastatistiku.util.DimensionConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dimensions")
@RequiredArgsConstructor
public class DimensionController {

    private final DimensionService dimensionService;
    private final DimensionConverter dimensionConverter;

    @PostMapping
    public ResponseEntity<DimensionResponse> create(@RequestBody DimensionRequest dimensionRequest) {
        Dimension saved = dimensionService.create(dimensionRequest);
        return ResponseEntity.ok(dimensionConverter.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<DimensionResponse>> getAll() {
        List<DimensionResponse> result = dimensionService.getAll().stream()
                .map(dimensionConverter::toResponse)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{dimensionId}")
    public ResponseEntity<DimensionResponse> getById(@PathVariable Long dimensionId) {
        Dimension d = dimensionService.getById(dimensionId);
        if (d == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dimensionConverter.toResponse(d));
    }

    @PutMapping("/{dimensionId}")
    public ResponseEntity<DimensionResponse> update(@PathVariable Long dimensionId, @RequestBody DimensionRequest dimensionRequest) {
        Dimension updated = dimensionService.update(dimensionId, dimensionRequest);
        return ResponseEntity.ok(dimensionConverter.toResponse(updated));
    }

    @DeleteMapping("/{dimensionId}")
    public ResponseEntity<?> delete(@PathVariable Long dimensionId) {
        dimensionService.delete(dimensionId);
        return ResponseEntity.noContent().build();
    }

}
