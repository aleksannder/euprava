package com.github.aleksannder.zavodzastatistiku.controller.population;

import com.github.aleksannder.zavodzastatistiku.dto.population.ExtremesDto;
import com.github.aleksannder.zavodzastatistiku.dto.population.ProjectionDto;
import com.github.aleksannder.zavodzastatistiku.dto.population.RegionValueDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.population.PopulationStat;
import com.github.aleksannder.zavodzastatistiku.service.population.PopulationStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/population")
@RequiredArgsConstructor
public class PopulationStatController {
    private final PopulationStatService service;

    @PreAuthorize("hasAnyRole('CITIZEN', 'ANALYST', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PopulationStat>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "year, desc") String[] sort
    ) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sort[0]);
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PopulationStat> getById(@PathVariable Long id) {
        PopulationStat stat = service.findById(id);
        return stat == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(stat);
    }

    @PreAuthorize("hasRole('ANALYST')")
    @PostMapping
    public ResponseEntity<PopulationStat> create(@RequestBody PopulationStat stat) {
        return ResponseEntity.ok(service.save(stat));
    }

    @PreAuthorize("hasRole('ANALYST')")
    @PutMapping("/{id}")
    public ResponseEntity<PopulationStat> update(@PathVariable Long id, @RequestBody PopulationStat updated) {
        PopulationStat existing = service.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        updated.setId(id);
        return ResponseEntity.ok(service.save(updated));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Specific cases

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'CITIZEN')")
    @GetMapping("/trend/{region}")
    public ResponseEntity<List<PopulationStat>> trend(@PathVariable Region region) {
        return ResponseEntity.ok(service.getStatsForRegion(region));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'CITIZEN')")
    @GetMapping("/avg-age")
    public ResponseEntity<List<RegionValueDto>> getAvgAge() {
        return ResponseEntity.ok(service.avgAgeByRegion());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'CITIZEN')")
    @GetMapping("/natural-growth")
    public ResponseEntity<List<RegionValueDto>> naturalGrowth() {
        return ResponseEntity.ok(service.naturalGrowth());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'CITIZEN')")
    @GetMapping("/extremes")
    public ResponseEntity<ExtremesDto> getExtremes() {
        return ResponseEntity.ok(service.extremes());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'CITIZEN')")
    @GetMapping("/projection/{region}")
    public ResponseEntity<List<ProjectionDto>> getProjection(@PathVariable Region region) {
        return ResponseEntity.ok(service.projection(region));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'CITIZEN')")
    @GetMapping("/share/{year}")
    public ResponseEntity<List<RegionValueDto>> share(@PathVariable int year) {
        return ResponseEntity.ok(service.populationShare(year));
    }

}