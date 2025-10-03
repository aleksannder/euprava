package com.github.aleksannder.zavodzastatistiku.controller.wage;

import com.github.aleksannder.zavodzastatistiku.dto.wage.WageStatDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.wage.WageStat;
import com.github.aleksannder.zavodzastatistiku.service.wage.WageStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wage")
public class WageStatController {

    private final WageStatService service;

    @PreAuthorize("hasAnyRole('CITIZEN', 'ANALYST', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Page<WageStat>> getAll(
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
    public ResponseEntity<WageStat> getById(@PathVariable Long id) {
        WageStat stat = service.findById(id);
        return stat == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(stat);
    }

    @PreAuthorize("hasRole('ANALYST')")
    @PostMapping
    public ResponseEntity<WageStat> create(@RequestBody WageStat stat) {
        return ResponseEntity.ok(service.save(stat));
    }

    @PreAuthorize("hasRole('ANALYST')")
    @PutMapping("/{id}")
    public ResponseEntity<WageStat> update(@PathVariable Long id, @RequestBody WageStat updated) {
        WageStat existing = service.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        updated.setId(id);
        return ResponseEntity.ok(service.save(updated));
    }

    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // specifics

    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN', 'CITIZEN')")
    @GetMapping("/trend/{region}")
    public ResponseEntity<List<WageStat>> getTrendsByRegion(@PathVariable Region region) {
        return ResponseEntity.ok(service.getTrendByRegion(region));
    }

    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN', 'CITIZEN')")
    @GetMapping("/comparison/{year}")
    public ResponseEntity<List<WageStat>> getComparisonsByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(service.getComparisonByYear(year));
    }

    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN', 'CITIZEN')")
    @GetMapping("/ranking")
    public ResponseEntity<List<Object[]>> getRegionsSortedByAverageWage() {
        return ResponseEntity.ok(service.getRegionsSortedByAverageWage());
    }

    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN', 'CITIZEN')")
    @GetMapping("/highlight/{year}")
    public ResponseEntity<WageStatDto> getHighlightByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(service.findRegionWithHighestGrowth(year));
    }
}
