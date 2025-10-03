package com.example.demo.controller;

import com.example.demo.dto.OruzjeRequest;
import com.example.demo.model.Korisnik;
import com.example.demo.model.Oruzje;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.service.OruzjeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oruzje")
@RequiredArgsConstructor
public class OruzjeController {

    private final OruzjeService service;
    private final KorisnikRepository korisnikRepository;


    @PostMapping("/zahtev/{authHeader}")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<Map<String, String>> podnesiZahtev(@RequestBody OruzjeRequest request,
                                                             @PathVariable String authHeader) {
        Map<String, String> response = new HashMap<>();
        try {
            Oruzje o = service.podnesiZahtev(authHeader, request);

            response.put("message", "Zahtev uspešno podnet!");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("message", "Došlo je do greške");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/svi-zahtevi/{authHeader}")
    @PreAuthorize("hasAnyRole('CITIZEN','EMPLOYER')")
    public ResponseEntity<List<Oruzje>> sviZahtevi(@PathVariable String authHeader) {
        Korisnik u = korisnikRepository.findByEmail(authHeader).orElse(null);

        if (u == null) {
            return ResponseEntity.notFound().build();
        }

        List<Oruzje> zahtevi = service.sviZahteviPoKorisnikuIliSvi(u.getRole(), u.getEmail());
        return ResponseEntity.ok(zahtevi);
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

    @PutMapping("/produzi-sve")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<Map<String, String>> produziSveZahteve() {
        Map<String, String> response = new HashMap<>();
        try {
            int brojProduzenih = service.produziSveZahteveKojiIsticuZaMesec();

            if (brojProduzenih == 0) {
                response.put("message", "Ne postoji zahtev koji ističe za manje od mesec dana.");
            } else {
                response.put("message", brojProduzenih + " zahteva uspešno produženo!");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Došlo je do greške prilikom produženja zahteva.");
            return ResponseEntity.status(500).body(response);
        }
    }

}
