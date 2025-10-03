package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.service.LicnaKartaService;
import com.example.demo.service.OruzjeService;
import com.example.demo.service.SaobracajnaDozvolaService;
import com.example.demo.service.VozackaDozvolaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/zahtevi")
@RequiredArgsConstructor
public class ZahteviController {

    private final LicnaKartaService licnaKartaService;
    private final OruzjeService oruzjeService;
    private final SaobracajnaDozvolaService saobracajnaService;
    private final VozackaDozvolaService vozackaService;
    private final KorisnikRepository korisnikRepository;

    @GetMapping("/moji/{authHeader}")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> sviMojiZahtevi(@PathVariable String authHeader) {
        Korisnik u = korisnikRepository.findByEmail(authHeader).orElse(null);
        if (u == null) { return ResponseEntity.notFound().build(); }
        Map<String, Object> response = new HashMap<>();
        response.put("licne_karte", licnaKartaService.prikaziSveZahteve(authHeader));
        response.put("oruzje", oruzjeService.sviZahteviPoKorisnikuIliSvi(u.getRole(),authHeader));
        response.put("saobracajne_dozvole", saobracajnaService.prikaziSveZahteve(authHeader));
        response.put("vozacke_dozvole", vozackaService.prikaziSveZahteve(authHeader));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/prijava-izgubljenog/{authHeader}")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<Map<String, String>> prijavaIzgubljenogDokumenta(
            @PathVariable String authHeader,
            @RequestBody Map<String, String> payload) {


        String tipDokumenta = payload.get("tip");
        Long dokumentId = Long.valueOf(payload.get("id"));
        String datumPrijave = payload.get("datumPrijave");

        boolean uspeh = false;

        switch (tipDokumenta) {
            case "Lična karta":
                licnaKartaService.prijaviIzgubljeni(dokumentId, authHeader);
                break;
            case "Oružje":
                oruzjeService.prijaviIzgubljeni(dokumentId, authHeader);
                break;
            case "Saobraćajna dozvola":
                saobracajnaService.prijaviIzgubljeni(dokumentId, authHeader);
                break;
            case "Vozačka dozvola":
                vozackaService.prijaviIzgubljeni(dokumentId, authHeader);
                break;
            default:
                throw new IllegalArgumentException("Nepoznat tip dokumenta");
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Dokument uspešno prijavljen kao izgubljen.");
        return ResponseEntity.ok(response);

    }

    @GetMapping("/maticna-knjiga/{authHeader}")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<Korisnik> izvodIzMaticneKnjige(@PathVariable String authHeader) {
        Korisnik korisnik = korisnikRepository.findByEmail(authHeader)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        return ResponseEntity.ok(korisnik);
    }

}
