package com.github.aleksannder.zavodzastatistiku.controller.gdp;

import com.github.aleksannder.zavodzastatistiku.dto.GdpGrowthHighlight;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.gdp.GdpStat;
import com.github.aleksannder.zavodzastatistiku.service.gdp.GdpStatService;
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
@RequestMapping("/api/gdp")
public class GdpStatController {

    private final GdpStatService service;

    @GetMapping
    public ResponseEntity<Page<GdpStat>> getAll(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "year, desc") String[] sort) {
        Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GdpStat> getById(@PathVariable Long id) {
        GdpStat stat = service.findById(id);
        return stat == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(stat);
    }

    @PostMapping
    public ResponseEntity<GdpStat> create(@RequestBody GdpStat stat) {
        return ResponseEntity.ok(service.save(stat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GdpStat> update(@PathVariable Long id, @RequestBody GdpStat updated) {
        GdpStat existing = service.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        updated.setId(id);
        return ResponseEntity.ok(service.save(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trend")
    public ResponseEntity<List<GdpStat>> getTrend() {
        return ResponseEntity.ok(service.getTrend());
    }

    @GetMapping("/cpi")
    public ResponseEntity<List<GdpStat>> getCpi() {
        return ResponseEntity.ok(service.getCpi());
    }

    @GetMapping("/growth/{year}/{region}")
    public ResponseEntity<GdpGrowthHighlight> getHighlight(@PathVariable Integer year, @PathVariable Region region) {
        return ResponseEntity.ok(service.gdpGrowthFromLastYear(year, region));
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<Page<GdpStat>> getAllStatsForRegion(@PathVariable Region region,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "year, desc") String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        return ResponseEntity.ok(service.getAllStatsForRegion(region, pageable));
    }
}
