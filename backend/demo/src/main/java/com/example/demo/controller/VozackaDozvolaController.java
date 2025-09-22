package com.example.demo.controller;

import com.example.demo.dto.VozackaDozvolaRequest;
import com.example.demo.model.Oruzje;
import com.example.demo.model.VozackaDozvola;
import com.example.demo.service.VozackaDozvolaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vozacka-dozvola")
public class VozackaDozvolaController {

    private final VozackaDozvolaService service;

    public VozackaDozvolaController(VozackaDozvolaService service) {
        this.service = service;
    }

    @PostMapping("/zahtev")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<String> podnesiZahtev(@RequestHeader("Authorization") String token,
                                                @RequestBody VozackaDozvolaRequest request) {
        VozackaDozvola vozacka = service.podnesiZahtev(token, request.getKategorije());
        if (vozacka == null) return ResponseEntity.badRequest().body("Već postoji aktivan zahtev ili dozvola!");
        return ResponseEntity.ok("Zahtev uspešno podnet!");
    }

    @PostMapping("/produzi")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<?> produzi(@RequestHeader("Authorization") String token) {
        try {
            List<VozackaDozvola> produzeno = service.produziVozacku(token);
            return ResponseEntity.ok(produzeno);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/svi-zahtevi")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<List<VozackaDozvola>> sviZahtevi(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.prikaziSveZahteve(token));
    }

    @PutMapping("/odobri/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Map<String, String>> odobri(@PathVariable Long id) {
        service.odobriZahtev(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Zahtev odobren!");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/odbij/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Map<String, String>> odbij(@PathVariable Long id) {
        service.odbijZahtev(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Zahtev odbijen!");
        return ResponseEntity.ok(response);
    }

}
