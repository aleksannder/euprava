package com.github.aleksannder.zavodzastatistiku.controller.traffic;

import com.github.aleksannder.zavodzastatistiku.dto.traffic.DangerousRegionDTO;
import com.github.aleksannder.zavodzastatistiku.dto.traffic.FatalitiesTrendDto;
import com.github.aleksannder.zavodzastatistiku.dto.traffic.TrafficSummaryDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.traffic.TrafficStat;
import com.github.aleksannder.zavodzastatistiku.service.traffic.TrafficStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/traffic")
public class TrafficStatController {

    private final TrafficStatService service;

    @GetMapping
    public ResponseEntity<Page<TrafficStat>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "year, desc") String[] sort) {
        Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, direction, sort[0]);
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrafficStat> getById(@PathVariable Long id) {
        TrafficStat stat = service.findById(id);
        return stat == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(stat);
    }

    @PostMapping
    public ResponseEntity<TrafficStat> create(@RequestBody TrafficStat stat) {
        return ResponseEntity.ok(service.save(stat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrafficStat> update(@PathVariable Long id, @RequestBody TrafficStat updated) {
        TrafficStat existing = service.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        updated.setId(id);
        return ResponseEntity.ok(service.save(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Specific use cases
    @GetMapping("/trend/{region}")
    public ResponseEntity<List<TrafficStat>> getTrendByRegion(@PathVariable Region region) {
        return ResponseEntity.ok(service.getTrendByRegion(region));
    }

    @GetMapping("/summary")
    public ResponseEntity<List<TrafficSummaryDto>> getSummaryByYear() {
        return ResponseEntity.ok(service.getSummaryOfVehiclesAndAccidentsByYear());
    }

    @GetMapping("/dangerous")
    public ResponseEntity<List<DangerousRegionDTO>> getTopDangerousRegions(
            @RequestParam(defaultValue = "2020") int yearFrom) {
        return ResponseEntity.ok(service.getTopDangerousRegions(yearFrom));
    }

    @GetMapping("/fatalities-trend")
    public ResponseEntity<List<FatalitiesTrendDto>> getFatalitiesTrend() {
        return ResponseEntity.ok(service.getFatalitiesTrend());
    }

}
