package com.example.demo.service;

import com.example.demo.dto.SaobracajnaDozvolaRequest;
import com.example.demo.model.*;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.repository.SaobracajnaDozvolaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaobracajnaDozvolaService {

    private final SaobracajnaDozvolaRepository repo;
    private final KorisnikRepository korisnikRepository;


    public SaobracajnaDozvola podnesiZahtev(String token, SaobracajnaDozvolaRequest req) {
        Korisnik korisnik = korisnikRepository.findByEmail(token)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        // Dohvati sve zahteve korisnika za proveru po tablicama
        List<SaobracajnaDozvola> sviZahtevi = repo.findAllByUser(korisnik);

        // Proveri da li postoji aktivan zahtev za iste tablice
        boolean postojiAktivanZahtev = sviZahtevi.stream()
                .anyMatch(s -> s.getPlateNumber().equalsIgnoreCase(req.getTablice()) &&
                        (s.getStatus() == StatusZahteva.CEKANJE || s.getStatus() == StatusZahteva.DOZVOLJEN));

        if (postojiAktivanZahtev) {
            // Ne može podneti zahtev za iste tablice dok postoji aktivan zahtev ili dozvola
            return null;
        }

        // Ako tablica postoji ali je prethodni zahtev odbijen, dozvoljeno je ponovo podneti zahtev
        boolean tablicaPostojiOdbijena = sviZahtevi.stream()
                .anyMatch(s -> s.getPlateNumber().equalsIgnoreCase(req.getTablice()) &&
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
                korisnik.getAddress()
        );
        saobracajna.setDrivingLicenseNumber(generisiBrojDozvole());
        return saobracajna;
    }


    public List<SaobracajnaDozvola> produzi(String token) {
        Korisnik korisnik = korisnikRepository.findByEmail(token)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        LocalDate danas = LocalDate.now();

        // Dohvati sve odobrene dozvole korisnika
        List<SaobracajnaDozvola> sveDozvole = repo.findAllByUser(korisnik);
        List<SaobracajnaDozvola> doProduzenja = sveDozvole.stream()
                .filter(s -> s.getStatus() == StatusZahteva.DOZVOLJEN)
                .filter(s -> !s.getValidUntil().isAfter(danas.plusMonths(1))) // ističe za manje od mesec dana
                .toList();

        if (doProduzenja.isEmpty()) {
            throw new IllegalStateException("Nema saobraćajnih dozvola spremnih za produženje.");
        }

        // Produži sve pronađene
        for (SaobracajnaDozvola s : doProduzenja) {
            s.setDateOfIssuing(danas);
            s.setValidUntil(danas.plusYears(5));
            s.setDrivingLicenseNumber(generisiBrojDozvole());
            repo.save(s);
        }

        return doProduzenja;
    }


    public List<SaobracajnaDozvola> prikaziSveZahteve(String token) {
        Korisnik korisnik = korisnikRepository.findByEmail(token)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        if (korisnik.getRole().name().equals("EMPLOYER")) {
            return repo.findAll();
        }
        return repo.findAllByUser(korisnik);
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
        if (!s.getUser().getEmail().equals(email)) {
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
