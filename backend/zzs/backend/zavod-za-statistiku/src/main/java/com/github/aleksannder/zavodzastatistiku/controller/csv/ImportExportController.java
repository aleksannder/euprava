package com.github.aleksannder.zavodzastatistiku.controller.csv;

import com.github.aleksannder.zavodzastatistiku.service.csv.CsvService;
import com.github.aleksannder.zavodzastatistiku.service.csv.InvalidCsvHeaderException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import-export")
@RequiredArgsConstructor
public class ImportExportController {

    private final CsvService csvService;

    @PostMapping("/{domain}/import")
    public ResponseEntity<?> importCsv(@PathVariable String domain, @RequestParam("file") MultipartFile file) {
        try {
            csvService.importCsv(domain, file);
            return ResponseEntity.ok().build();
        } catch (InvalidCsvHeaderException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{domain}/export")
    public ResponseEntity<Resource> exportCsv(@PathVariable String domain) {
        try {
            return csvService.exportCsv(domain);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
