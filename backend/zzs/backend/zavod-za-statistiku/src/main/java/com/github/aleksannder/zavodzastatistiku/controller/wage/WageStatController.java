package com.github.aleksannder.zavodzastatistiku.controller.wage;

import com.github.aleksannder.zavodzastatistiku.dto.WageStatDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.wage.WageStat;
import com.github.aleksannder.zavodzastatistiku.service.wage.WageStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wage")
public class WageStatController {

    private final WageStatService service;

    @GetMapping
    public ResponseEntity<List<WageStat>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WageStat> getById(@PathVariable Long id) {
        WageStat stat = service.findById(id);
        return stat == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(stat);
    }

    @PostMapping
    public ResponseEntity<WageStat> create(@RequestBody WageStat stat) {
        return ResponseEntity.ok(service.save(stat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WageStat> update(@PathVariable Long id, @RequestBody WageStat updated) {
        WageStat existing = service.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        updated.setId(id);
        return ResponseEntity.ok(service.save(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // specifics

    @GetMapping("/trend/{region}")
    public ResponseEntity<List<WageStat>> getTrendsByRegion(@PathVariable Region region) {
        return ResponseEntity.ok(service.getTrendByRegion(region));
    }

    @GetMapping("/comparison/{year}")
    public ResponseEntity<List<WageStat>> getComparisonsByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(service.getComparisonByYear(year));
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<Object[]>> getRegionsSortedByAverageWage() {
        return ResponseEntity.ok(service.getRegionsSortedByAverageWage());
    }

    @GetMapping("/highlight/{year}")
    public ResponseEntity<WageStatDto> getHighlightByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(service.findRegionWithHighestGrowth(year));
    }
}
