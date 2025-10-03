package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.domain.DomainRequest;
import com.github.aleksannder.zavodzastatistiku.dto.domain.DomainResponse;
import com.github.aleksannder.zavodzastatistiku.dto.unit.UnitRequest;
import com.github.aleksannder.zavodzastatistiku.dto.unit.UnitResponse;
import com.github.aleksannder.zavodzastatistiku.model.Unit;
import com.github.aleksannder.zavodzastatistiku.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {
    private final UnitService unitService;

    @PostMapping
    public ResponseEntity<UnitResponse> createUnit(@RequestBody UnitRequest req) {
        Unit saved = unitService.create(req);
        return ResponseEntity.ok(new UnitResponse(saved.getId(), saved.getCode(), saved.getName()));
    }

    @DeleteMapping("/{unitId}")
    public ResponseEntity<?> deleteUnit(@PathVariable Long unitId) {
        unitService.deleteUnit(unitId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{unitId}")
    public ResponseEntity<UnitResponse> getUnitById(@PathVariable Long unitId) {
        Unit u = unitService.getUnitById(unitId);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new UnitResponse(u.getId(), u.getCode(), u.getName()));
    }

    @GetMapping
    public ResponseEntity<List<UnitResponse>> getAllUnits() {
        List<UnitResponse> units = unitService.getAllUnits().stream()
                .map(u -> new UnitResponse(u.getId(), u.getCode(), u.getName()))
                .toList();
        if (units.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(units);
    }

    @PutMapping("/{unitId}")
    public ResponseEntity<UnitResponse> updateUnit(@PathVariable Long unitId, @RequestBody UnitRequest req) {
        Unit updated = unitService.updateUnit(req, unitId);
        return ResponseEntity.ok(new UnitResponse(updated.getId(), updated.getCode(), updated.getName()));
    }


}
