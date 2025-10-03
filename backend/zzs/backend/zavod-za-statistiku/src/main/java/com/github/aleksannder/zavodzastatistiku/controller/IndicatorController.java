package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.indicator.IndicatorRequest;
import com.github.aleksannder.zavodzastatistiku.dto.indicator.IndicatorResponse;
import com.github.aleksannder.zavodzastatistiku.model.Indicator;
import com.github.aleksannder.zavodzastatistiku.service.IndicatorService;
import com.github.aleksannder.zavodzastatistiku.util.IndicatorConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indicators")
@RequiredArgsConstructor
public class IndicatorController {

    private final IndicatorService service;

    @PostMapping
    public ResponseEntity<IndicatorResponse> create(@RequestBody IndicatorRequest r) {
        Indicator saved =  service.create(r);
        return ResponseEntity.ok(IndicatorConverter.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<IndicatorResponse>> getAll() {
        return ResponseEntity.ok(
                service.getAll().stream().map(IndicatorConverter::toResponse).toList()
        );
    }

    @GetMapping("/{indicatorId}")
    public ResponseEntity<IndicatorResponse> getById(@PathVariable Long indicatorId) {
        Indicator i =  service.getById(indicatorId);
        if (i == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(IndicatorConverter.toResponse(i));
    }

    @PutMapping("/{indicatorId}")
    public ResponseEntity<IndicatorResponse> update(@PathVariable Long indicatorId, @RequestBody IndicatorRequest r) {
        Indicator updated = service.update(indicatorId, r);
        return ResponseEntity.ok(IndicatorConverter.toResponse(updated));
    }

    @DeleteMapping("/{indicatorId}")
    public ResponseEntity<?> delete(@PathVariable Long indicatorId) {
        service.delete(indicatorId);
        return ResponseEntity.ok().build();
    }
}
