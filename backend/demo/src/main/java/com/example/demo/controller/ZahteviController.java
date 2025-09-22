package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.security.JwtService;
import com.example.demo.service.LicnaKartaService;
import com.example.demo.service.OruzjeService;
import com.example.demo.service.SaobracajnaDozvolaService;
import com.example.demo.service.VozackaDozvolaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/zahtevi")
public class ZahteviController {

    private final LicnaKartaService licnaKartaService;
    private final OruzjeService oruzjeService;
    private final SaobracajnaDozvolaService saobracajnaService;
    private final VozackaDozvolaService vozackaService;
    private final JwtService jwtService;
    private final KorisnikRepository korisnikRepository;

    public ZahteviController(LicnaKartaService licnaKartaService,
                             OruzjeService oruzjeService,
                             SaobracajnaDozvolaService saobracajnaService,
                             VozackaDozvolaService vozackaService,
                             JwtService jwtService,
                             KorisnikRepository korisnikRepository) {
        this.licnaKartaService = licnaKartaService;
        this.oruzjeService = oruzjeService;
        this.saobracajnaService = saobracajnaService;
        this.vozackaService = vozackaService;
        this.jwtService = jwtService;
        this.korisnikRepository = korisnikRepository;
    }


    @GetMapping("/moji")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> sviMojiZahtevi(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        Map<String, Object> response = new HashMap<>();
        response.put("licne_karte", licnaKartaService.prikaziSveZahteve(authHeader));
        response.put("oruzje", oruzjeService.sviZahteviPoKorisnikuIliSvi(jwtService.extractRole(token), email));
        response.put("saobracajne_dozvole", saobracajnaService.prikaziSveZahteve(authHeader));
        response.put("vozacke_dozvole", vozackaService.prikaziSveZahteve(authHeader));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/prijava-izgubljenog")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<Map<String, String>> prijavaIzgubljenogDokumenta(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> payload) {

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        String tipDokumenta = payload.get("tip");
        Long dokumentId = Long.valueOf(payload.get("id"));
        String datumPrijave = payload.get("datumPrijave");

        boolean uspeh = false;

        switch (tipDokumenta) {
            case "Lična karta":
                licnaKartaService.prijaviIzgubljeni(dokumentId, email);
                break;
            case "Oružje":
                oruzjeService.prijaviIzgubljeni(dokumentId, email);
                break;
            case "Saobraćajna dozvola":
                saobracajnaService.prijaviIzgubljeni(dokumentId, email);
                break;
            case "Vozačka dozvola":
                vozackaService.prijaviIzgubljeni(dokumentId, email);
                break;
            default:
                throw new IllegalArgumentException("Nepoznat tip dokumenta");
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Dokument uspešno prijavljen kao izgubljen.");
        return ResponseEntity.ok(response);

    }

    @GetMapping("/maticna-knjiga")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<Korisnik> izvodIzMaticneKnjige(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        return ResponseEntity.ok(korisnik);
    }

}
