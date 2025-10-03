package com.example.demo.controller;

import com.example.demo.model.LicnaKarta;
import com.example.demo.service.LicnaKartaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasRole('CITIZEN')")
    @PostMapping("/zahtev/{userEmail}")
    public ResponseEntity<String> podnesiZahtev(@PathVariable String userEmail) {
        LicnaKarta licnaKarta = licnaKartaService.podnesiZahtevIzTokena(userEmail, "Srbija");

        if (licnaKarta == null) {
            return ResponseEntity.badRequest().body("Korisnik već ima podnet zahtev ili izdata ličnu kartu!");
        }

        return ResponseEntity.ok("Zahtev za izdavanje lične karte je uspešno podnet!");
    }

    @PostMapping("/produzi/{userEmail}")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<String> produziLicnu(@PathVariable String userEmail) {
        try {
            licnaKartaService.produziLicnuKartu(userEmail);
            return ResponseEntity.ok(" Lična karta je uspešno produžena!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @GetMapping("/svi-zahtevi/{userEmail}")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<List<LicnaKarta>> sviZahtevi(@PathVariable String userEmail) {
        List<LicnaKarta> licneKarte = licnaKartaService.prikaziSveZahteve(userEmail);
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
