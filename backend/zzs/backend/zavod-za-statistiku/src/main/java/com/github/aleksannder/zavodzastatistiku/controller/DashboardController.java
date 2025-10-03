package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.Highlights;
import com.github.aleksannder.zavodzastatistiku.service.MupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final MupService mupService;

    @PreAuthorize("hasRole('CITIZEN')")
    @GetMapping("/highlights/{userEmail}")
    public ResponseEntity<Highlights> getHighlights(@PathVariable String userEmail) {
        return ResponseEntity.ok(mupService.getHighlights(userEmail));
    }

}
