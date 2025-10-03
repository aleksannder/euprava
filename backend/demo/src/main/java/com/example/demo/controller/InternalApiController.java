package com.example.demo.controller;

import com.example.demo.service.LicnaKartaService;
import com.example.demo.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class InternalApiController {

    private final StatsService statsService;

    @PreAuthorize("hasAnyRole('CITIZEN', 'EMPLOYER')")
    @GetMapping("/identification-cards")
    public Long countIdentificationCards() {
        return this.statsService.countIds();
    }

    @PreAuthorize("hasAnyRole('CITIZEN', 'EMPLOYER')")
    @GetMapping("/vehicles/licences")
    public Long countVehicleLicences() {
        return this.statsService.countVehicleLicences();
    }

    @PreAuthorize("hasAnyRole('CITIZEN', 'EMPLOYER')")
    @GetMapping("/drivers-licenses")
    public Long countDriversLicenses() {
        return this.statsService.countDriversLicenses();
    }

    @PreAuthorize("hasAnyRole('CITIZEN', 'EMPLOYER')")
    @GetMapping("/guns/permits")
    public Long countGunPermits() {
        return this.statsService.countGunPermits();
    }
}
