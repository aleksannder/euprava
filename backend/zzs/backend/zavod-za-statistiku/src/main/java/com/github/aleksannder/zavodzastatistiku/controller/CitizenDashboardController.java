package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.CitizenDashboardDto;
import com.github.aleksannder.zavodzastatistiku.model.User;
import com.github.aleksannder.zavodzastatistiku.service.CitizenDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class CitizenDashboardController {

    private final CitizenDashboardService citizenDashboardService;

    @GetMapping("/my-region")
    public ResponseEntity<CitizenDashboardDto> getMyRegionDashboard(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(citizenDashboardService.getDashboardForRegion(user.getRegion()));
    }
}
