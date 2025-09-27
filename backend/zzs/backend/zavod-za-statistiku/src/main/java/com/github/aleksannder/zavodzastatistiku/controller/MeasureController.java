package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.measure.MeasureRequest;
import com.github.aleksannder.zavodzastatistiku.dto.measure.MeasureResponse;
import com.github.aleksannder.zavodzastatistiku.model.Measure;
import com.github.aleksannder.zavodzastatistiku.service.MeasureService;
import com.github.aleksannder.zavodzastatistiku.util.MeasureConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.aleksannder.zavodzastatistiku.util.MeasureConverter.toResponse;

@RestController
@RequestMapping("/api/measures")
@RequiredArgsConstructor
public class MeasureController {

    private final MeasureService measureService;

    @PostMapping
    public ResponseEntity<MeasureResponse> create(@RequestBody MeasureRequest req) {
        Measure saved = measureService.create(req);
        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<MeasureResponse>> getAll() {
        return ResponseEntity.ok(
                measureService.getAll().stream().map(MeasureConverter::toResponse).toList()
        );
    }

    @GetMapping("/{measureId}")
    public ResponseEntity<MeasureResponse> getById(@PathVariable Long measureId) {
        Measure m = measureService.getById(measureId);
        if (m == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(m));
    }

    @PutMapping("/{measureId}")
    public ResponseEntity<MeasureResponse> update(@PathVariable Long measureId, @RequestBody MeasureRequest req) {
        Measure updated = measureService.update(measureId, req);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{measureId}")
    public ResponseEntity<?> delete(@PathVariable Long measureId) {
        measureService.delete(measureId);
        return ResponseEntity.noContent().build();
    }
}
