package com.example.demo.controller;

import com.example.demo.dto.SaobracajnaDozvolaRequest;
import com.example.demo.model.SaobracajnaDozvola;
import com.example.demo.service.SaobracajnaDozvolaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/saobracajna-dozvola")
public class SaobracajnaDozvolaController {

    private final SaobracajnaDozvolaService service;

    public SaobracajnaDozvolaController(SaobracajnaDozvolaService service) {
        this.service = service;
    }

    @PostMapping("/zahtev")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<String> podnesiZahtev(@RequestHeader("Authorization") String token,
                                                @RequestBody SaobracajnaDozvolaRequest request) {
        try {
            SaobracajnaDozvola s = service.podnesiZahtev(token, request);
            if (s == null)
                return ResponseEntity.badRequest().body("Već postoji aktivan zahtev ili dozvola!");
            return ResponseEntity.ok("Zahtev uspešno podnet!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Greška pri kreiranju zahteva.");
        }
    }

    @PostMapping("/produzi")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<String> produzi(@RequestHeader("Authorization") String token) {
        try {
            service.produzi(token);
            return ResponseEntity.ok("Dozvola uspešno produžena!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/svi-zahtevi")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<List<SaobracajnaDozvola>> sviZahtevi(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.prikaziSveZahteve(token));
    }

    @PutMapping("/odobri/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Map<String, String>> odobri(@PathVariable Long id) {
        service.odobriZahtev(id);
        return ResponseEntity.ok(Map.of("message", "Zahtev odobren!"));
    }

    @PutMapping("/odbij/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Map<String, String>> odbij(@PathVariable Long id) {
        service.odbijZahtev(id);
        return ResponseEntity.ok(Map.of("message", "Zahtev odbijen!"));
    }

}
