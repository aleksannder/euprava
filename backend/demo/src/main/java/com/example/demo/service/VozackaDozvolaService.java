package com.example.demo.service;

import com.example.demo.model.Korisnik;
import com.example.demo.model.StatusZahteva;
import com.example.demo.model.VozackaDozvola;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.repository.VozackaDozvolaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VozackaDozvolaService {

    private final VozackaDozvolaRepository vozackaRepository;
    private final KorisnikRepository korisnikRepository;


    public VozackaDozvola podnesiZahtev(String token, List<String> noveKategorije) {
        Korisnik korisnik = korisnikRepository.findByEmail(token)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        List<VozackaDozvola> sviZahtevi = vozackaRepository.findAllByUser(korisnik);

        for (String kategorija : noveKategorije) {
            boolean postojiAktivanZaKategoriju = sviZahtevi.stream()
                    .filter(v -> v.getCategories().contains(kategorija))
                    .anyMatch(v -> v.getStatus() == StatusZahteva.CEKANJE || v.getStatus() == StatusZahteva.DOZVOLJEN);

            if (postojiAktivanZaKategoriju) {
                return null;
            }
        }

        VozackaDozvola vozacka = VozackaDozvola.kreiraj(korisnik, korisnik.getCity(), noveKategorije);
        vozacka.setLicenseNumber(generisiBrojDozvole());
        return vozackaRepository.save(vozacka);
    }



    public List<VozackaDozvola> produziVozacku(String token) {
        Korisnik korisnik = korisnikRepository.findByEmail(token)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        LocalDate danas = LocalDate.now();

        List<VozackaDozvola> sveDozvole = vozackaRepository.findAllByUser(korisnik);

        List<VozackaDozvola> doProduzenja = sveDozvole.stream()
                .filter(v -> v.getStatus() == StatusZahteva.DOZVOLJEN)
                .filter(v -> !v.getValidUntil().isAfter(danas.plusMonths(1)))
                .toList();

        if (doProduzenja.isEmpty()) {
            throw new IllegalStateException("Nema vozačkih dozvola spremnih za produženje.");
        }

        for (VozackaDozvola v : doProduzenja) {
            v.setDateOfIssuing(danas);
            v.setValidUntil(danas.plusYears(10));
            v.setLicenseNumber(generisiBrojDozvole());
            vozackaRepository.save(v);
        }

        return doProduzenja;
    }

    public List<VozackaDozvola> prikaziSveZahteve(String token) {
        Korisnik korisnik = korisnikRepository.findByEmail(token)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        if (korisnik.getRole().name().equals("EMPLOYER")) {
            return vozackaRepository.findAll();
        }

        return vozackaRepository.findAllByUser(korisnik);
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

        if (!v.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("Nije vaš dokument");
        }

        if (v.getStatus() != StatusZahteva.DOZVOLJEN) {
            throw new IllegalStateException("Dokument nije aktivan");
        }

        v.setStatus(StatusZahteva.IZGUBLJEN);
        return vozackaRepository.save(v);
    }

}
