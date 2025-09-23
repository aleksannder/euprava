package com.example.demo.service;

import com.example.demo.model.Korisnik;
import com.example.demo.model.StatusZahteva;
import com.example.demo.model.VozackaDozvola;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.repository.VozackaDozvolaRepository;
import com.example.demo.security.JwtService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VozackaDozvolaService {

    private final VozackaDozvolaRepository vozackaRepository;
    private final KorisnikRepository korisnikRepository;
    private final JwtService jwtService;

    public VozackaDozvolaService(VozackaDozvolaRepository vozackaRepository,
                                 KorisnikRepository korisnikRepository,
                                 JwtService jwtService) {
        this.vozackaRepository = vozackaRepository;
        this.korisnikRepository = korisnikRepository;
        this.jwtService = jwtService;
    }

    public VozackaDozvola podnesiZahtev(String token, List<String> noveKategorije) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        String email = jwtService.extractEmail(token);

        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        List<VozackaDozvola> sviZahtevi = vozackaRepository.findAllByKorisnik(korisnik);

        for (String kategorija : noveKategorije) {
            boolean postojiAktivanZaKategoriju = sviZahtevi.stream()
                    .filter(v -> v.getKategorije().contains(kategorija))
                    .anyMatch(v -> v.getStatus() == StatusZahteva.CEKANJE || v.getStatus() == StatusZahteva.DOZVOLJEN);

            if (postojiAktivanZaKategoriju) {
                return null;
            }
        }

        VozackaDozvola vozacka = VozackaDozvola.kreiraj(korisnik, korisnik.getGrad(), noveKategorije);
        vozacka.setBrojDozvole(generisiBrojDozvole());
        return vozackaRepository.save(vozacka);
    }



    public List<VozackaDozvola> produziVozacku(String token) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        String email = jwtService.extractEmail(token);

        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        LocalDate danas = LocalDate.now();

        List<VozackaDozvola> sveDozvole = vozackaRepository.findAllByKorisnik(korisnik);

        List<VozackaDozvola> doProduzenja = sveDozvole.stream()
                .filter(v -> v.getStatus() == StatusZahteva.DOZVOLJEN)
                .filter(v -> !v.getDatumVazenja().isAfter(danas.plusMonths(1)))
                .toList();

        if (doProduzenja.isEmpty()) {
            throw new IllegalStateException("Nema vozačkih dozvola spremnih za produženje.");
        }

        for (VozackaDozvola v : doProduzenja) {
            v.setDatumIzdavanja(danas);
            v.setDatumVazenja(danas.plusYears(10));
            v.setBrojDozvole(generisiBrojDozvole());
            vozackaRepository.save(v);
        }

        return doProduzenja;
    }

    public List<VozackaDozvola> prikaziSveZahteve(String token) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        String email = jwtService.extractEmail(token);

        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        if (korisnik.getRola().name().equals("EMPLOYER")) {
            return vozackaRepository.findAll();
        }

        return vozackaRepository.findAllByKorisnik(korisnik);
    }

    public VozackaDozvola odobriZahtev(Long id) {
        VozackaDozvola vozacka = vozackaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zahtev ne postoji"));
        vozacka.setStatus(StatusZahteva.DOZVOLJEN);
        return vozackaRepository.save(vozacka);
    }

    public VozackaDozvola odbijZahtev(Long id) {
        VozackaDozvola vozacka = vozackaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zahtev ne postoji"));
        vozacka.setStatus(StatusZahteva.ODBIJEN);
        return vozackaRepository.save(vozacka);
    }

    private String generisiBrojDozvole() {
        int min = 000_000_000;
        int max = 999_999_999;
        int broj = min + (int)(Math.random() * ((max - min) + 1));
        return String.valueOf(broj);
    }

    public VozackaDozvola prijaviIzgubljeni(Long vozackaId, String email) {
        VozackaDozvola v = vozackaRepository.findById(vozackaId)
                .orElseThrow(() -> new IllegalArgumentException("Vozačka dozvola ne postoji"));

        if (!v.getKorisnik().getEmail().equals(email)) {
            throw new IllegalArgumentException("Nije vaš dokument");
        }

        if (v.getStatus() != StatusZahteva.DOZVOLJEN) {
            throw new IllegalStateException("Dokument nije aktivan");
        }

        v.setStatus(StatusZahteva.IZGUBLJEN);
        return vozackaRepository.save(v);
    }

}
