package com.github.aleksannder.zavodzastatistiku.controller.population;

import com.github.aleksannder.zavodzastatistiku.dto.population.ExtremesDto;
import com.github.aleksannder.zavodzastatistiku.dto.population.ProjectionDto;
import com.github.aleksannder.zavodzastatistiku.dto.population.RegionValueDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.population.PopulationStat;
import com.github.aleksannder.zavodzastatistiku.service.population.PopulationStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/population")
@RequiredArgsConstructor
public class PopulationStatController {
    private final PopulationStatService service;

    @GetMapping
    public ResponseEntity<List<PopulationStat>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PopulationStat> getById(@PathVariable Long id) {
        PopulationStat stat = service.findById(id);
        return stat == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(stat);
    }

    @PostMapping
    public ResponseEntity<PopulationStat> create(@RequestBody PopulationStat stat) {
        return ResponseEntity.ok(service.save(stat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PopulationStat> update(@PathVariable Long id, @RequestBody PopulationStat updated) {
        PopulationStat existing = service.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        updated.setId(id);
        return ResponseEntity.ok(service.save(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Specific cases

    @GetMapping("/trend/{region}")
    public ResponseEntity<List<PopulationStat>> trend(@PathVariable Region region) {
        return ResponseEntity.ok(service.getStatsForRegion(region));
    }

    @GetMapping("/avg-age")
    public ResponseEntity<List<RegionValueDto>> getAvgAge() {
        return ResponseEntity.ok(service.avgAgeByRegion());
    }

    @GetMapping("/natural-growth")
    public ResponseEntity<List<RegionValueDto>> naturalGrowth() {
        return ResponseEntity.ok(service.naturalGrowth());
    }

    @GetMapping("/extremes")
    public ResponseEntity<ExtremesDto> getExtremes() {
        return ResponseEntity.ok(service.extremes());
    }

    @GetMapping("/projection/{region}")
    public ResponseEntity<List<ProjectionDto>> getProjection(@PathVariable Region region) {
        return ResponseEntity.ok(service.projection(region));
    }

    @GetMapping("/share/{year}")
    public ResponseEntity<List<RegionValueDto>> share(@PathVariable int year) {
        return ResponseEntity.ok(service.populationShare(year));
    }

}