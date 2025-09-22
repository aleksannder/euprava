package com.example.demo.service;

import com.example.demo.dto.SaobracajnaDozvolaRequest;
import com.example.demo.model.*;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.repository.SaobracajnaDozvolaRepository;
import com.example.demo.security.JwtService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SaobracajnaDozvolaService {

    private final SaobracajnaDozvolaRepository repo;
    private final KorisnikRepository korisnikRepository;
    private final JwtService jwtService;

    public SaobracajnaDozvolaService(SaobracajnaDozvolaRepository repo,
                                     KorisnikRepository korisnikRepository,
                                     JwtService jwtService) {
        this.repo = repo;
        this.korisnikRepository = korisnikRepository;
        this.jwtService = jwtService;
    }

    public SaobracajnaDozvola podnesiZahtev(String token, SaobracajnaDozvolaRequest req) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        String email = jwtService.extractEmail(token);

        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        // Dohvati sve zahteve korisnika za proveru po tablicama
        List<SaobracajnaDozvola> sviZahtevi = repo.findAllByKorisnik(korisnik);

        // Proveri da li postoji aktivan zahtev za iste tablice
        boolean postojiAktivanZahtev = sviZahtevi.stream()
                .anyMatch(s -> s.getTablice().equalsIgnoreCase(req.getTablice()) &&
                        (s.getStatus() == StatusZahteva.CEKANJE || s.getStatus() == StatusZahteva.DOZVOLJEN));

        if (postojiAktivanZahtev) {
            // Ne može podneti zahtev za iste tablice dok postoji aktivan zahtev ili dozvola
            return null;
        }

        // Ako tablica postoji ali je prethodni zahtev odbijen, dozvoljeno je ponovo podneti zahtev
        boolean tablicaPostojiOdbijena = sviZahtevi.stream()
                .anyMatch(s -> s.getTablice().equalsIgnoreCase(req.getTablice()) &&
                        s.getStatus() == StatusZahteva.ODBIJEN);

        if (tablicaPostojiOdbijena) {
            // Dozvoljeno je podneti zahtev ponovo za iste tablice
            SaobracajnaDozvola saobracajna = kreirajSaobracajnu(korisnik, req);
            return repo.save(saobracajna);
        }

        // Ako tablica ne postoji kod korisnika, dozvoljeno je podneti zahtev
        SaobracajnaDozvola saobracajna = kreirajSaobracajnu(korisnik, req);
        return repo.save(saobracajna);
    }

    private SaobracajnaDozvola kreirajSaobracajnu(Korisnik korisnik, SaobracajnaDozvolaRequest req) {
        SaobracajnaDozvola saobracajna = SaobracajnaDozvola.kreiraj(
                korisnik,
                req.getMarka(),
                req.getModel(),
                req.getKubikaza(),
                req.getGodiste(),
                req.getVrstaPogona(),
                req.getTablice(),
                korisnik.getAdresa()
        );
        saobracajna.setBrojDozvole(generisiBrojDozvole());
        return saobracajna;
    }


    public List<SaobracajnaDozvola> produzi(String token) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        String email = jwtService.extractEmail(token);

        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        LocalDate danas = LocalDate.now();

        // Dohvati sve odobrene dozvole korisnika
        List<SaobracajnaDozvola> sveDozvole = repo.findAllByKorisnik(korisnik);
        List<SaobracajnaDozvola> doProduzenja = sveDozvole.stream()
                .filter(s -> s.getStatus() == StatusZahteva.DOZVOLJEN)
                .filter(s -> !s.getDatumVazenja().isAfter(danas.plusMonths(1))) // ističe za manje od mesec dana
                .toList();

        if (doProduzenja.isEmpty()) {
            throw new IllegalStateException("Nema saobraćajnih dozvola spremnih za produženje.");
        }

        // Produži sve pronađene
        for (SaobracajnaDozvola s : doProduzenja) {
            s.setDatumIzdavanja(danas);
            s.setDatumVazenja(danas.plusYears(5));
            s.setBrojDozvole(generisiBrojDozvole());
            repo.save(s);
        }

        return doProduzenja;
    }


    public List<SaobracajnaDozvola> prikaziSveZahteve(String token) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        String email = jwtService.extractEmail(token);

        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        if (korisnik.getRola().name().equals("EMPLOYER")) {
            return repo.findAll();
        }
        return repo.findAllByKorisnik(korisnik);
    }

    public SaobracajnaDozvola odobriZahtev(Long id) {
        SaobracajnaDozvola s = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zahtev ne postoji"));
        s.setStatus(StatusZahteva.DOZVOLJEN);
        return repo.save(s);
    }

    public SaobracajnaDozvola odbijZahtev(Long id) {
        SaobracajnaDozvola s = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zahtev ne postoji"));
        s.setStatus(StatusZahteva.ODBIJEN);
        return repo.save(s);
    }

    private String generisiBrojDozvole() {
        int min = 100_000_000;
        int max = 999_999_999;
        int broj = min + (int)(Math.random() * ((max - min) + 1));
        return String.valueOf(broj);
    }
    public SaobracajnaDozvola prijaviIzgubljeni(Long dozvolaId, String email) {
        SaobracajnaDozvola s = repo.findById(dozvolaId)
                .orElseThrow(() -> new IllegalArgumentException("Saobraćajna dozvola ne postoji"));

        // Proveravamo da li dokument pripada korisniku
        if (!s.getKorisnik().getEmail().equals(email)) {
            throw new IllegalArgumentException("Nije vaš dokument");
        }

        // Može se prijaviti samo ako je aktivan
        if (s.getStatus() != StatusZahteva.DOZVOLJEN) {
            throw new IllegalStateException("Dokument nije aktivan");
        }

        // Postavljamo status na IZGUBLJEN
        s.setStatus(StatusZahteva.IZGUBLJEN);
        return repo.save(s);
    }


}
