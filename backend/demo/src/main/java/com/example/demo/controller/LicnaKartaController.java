package com.example.demo.controller;

import com.example.demo.dto.LicnaKartaRequest;
import com.example.demo.model.LicnaKarta;
import com.example.demo.service.LicnaKartaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/licna-karta")
public class LicnaKartaController {

    private final LicnaKartaService licnaKartaService;

    public LicnaKartaController(LicnaKartaService licnaKartaService) {
        this.licnaKartaService = licnaKartaService;
    }

    @PostMapping("/zahtev")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<String> podnesiZahtev(@RequestHeader("Authorization") String token) {
        LicnaKarta licnaKarta = licnaKartaService.podnesiZahtevIzTokena(token, "Srbija");

        if (licnaKarta == null) {
            return ResponseEntity.badRequest().body("Korisnik već ima podnet zahtev ili izdata ličnu kartu!");
        }

        return ResponseEntity.ok("Zahtev za izdavanje lične karte je uspešno podnet!");
    }

    @PostMapping("/produzi")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<String> produziLicnu(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            licnaKartaService.produziLicnuKartu(authorizationHeader);
            return ResponseEntity.ok(" Lična karta je uspešno produžena!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @GetMapping("/svi-zahtevi")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<List<LicnaKarta>> sviZahtevi(@RequestHeader("Authorization") String authorizationHeader) {
        List<LicnaKarta> licneKarte = licnaKartaService.prikaziSveZahteve(authorizationHeader);
        return ResponseEntity.ok(licneKarte);
    }

    @PutMapping("/odobri/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Map<String, String>> odobriZahtev(@PathVariable Long id) {
        licnaKartaService.odobriZahtev(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Zahtev je odobren!");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/odbij/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Map<String, String>> odbijZahtev(@PathVariable Long id) {
        licnaKartaService.odbijZahtev(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Zahtev je odbijen!");
        return ResponseEntity.ok(response);
    }

}
