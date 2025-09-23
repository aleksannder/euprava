package com.example.demo.service;

import com.example.demo.model.Korisnik;
import com.example.demo.model.LicnaKarta;
import com.example.demo.model.Role;
import com.example.demo.model.StatusZahteva;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.repository.LicnaKartaRepository;
import com.example.demo.security.JwtService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LicnaKartaService {

    private final LicnaKartaRepository licnaKartaRepository;
    private final KorisnikRepository korisnikRepository;
    private final JwtService jwtService;

    public LicnaKartaService(LicnaKartaRepository licnaKartaRepository,
                             KorisnikRepository korisnikRepository,
                             JwtService jwtService) {
        this.licnaKartaRepository = licnaKartaRepository;
        this.korisnikRepository = korisnikRepository;
        this.jwtService = jwtService;
    }

    public LicnaKarta podnesiZahtevIzTokena(String token, String drzava) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String email = jwtService.extractEmail(token);

        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        List<LicnaKarta> licne = licnaKartaRepository.findAllByKorisnik(korisnik);

        boolean postojiAktivanZahtev = licne.stream()
                .anyMatch(l -> l.getStatus() == StatusZahteva.CEKANJE || l.getStatus() == StatusZahteva.DOZVOLJEN);

        if (postojiAktivanZahtev) {
            return null;
        }

        LicnaKarta licnaKarta = new LicnaKarta();
        licnaKarta.setKorisnik(korisnik);
        licnaKarta.setIme(korisnik.getIme());
        licnaKarta.setPrezime(korisnik.getPrezime());
        licnaKarta.setJmbg(korisnik.getJmbg());
        licnaKarta.setDatumRodjenja(korisnik.getDatumRodjenja());
        licnaKarta.setPol(korisnik.getPol());
        licnaKarta.setGrad(korisnik.getGrad());
        licnaKarta.setDrzava(drzava);
        licnaKarta.setStatus(StatusZahteva.CEKANJE);
        licnaKarta.setRegBroj(UUID.randomUUID().toString());
        licnaKarta.setDatumIzdavanja(LocalDate.now());
        licnaKarta.setDatumVazenja(LocalDate.now().plusYears(10));

        return licnaKartaRepository.save(licnaKarta);
    }

    public LicnaKarta produziLicnuKartu(String authorizationHeader) {
        if (authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        }

        String email = jwtService.extractEmail(authorizationHeader);

        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        Optional<LicnaKarta> aktivnaLicnaOpt = licnaKartaRepository.findByKorisnikAndStatus(korisnik, StatusZahteva.DOZVOLJEN);

        if (aktivnaLicnaOpt.isPresent()) {
            LicnaKarta licna = aktivnaLicnaOpt.get();
            LocalDate danas = LocalDate.now();
            LocalDate datumIsteka = licna.getDatumVazenja();

            if (datumIsteka.isAfter(danas.plusMonths(1))) {
                throw new IllegalStateException("Lična karta još nije spremna za produženje (više od mesec dana do isteka).");
            }

            licna.setDatumIzdavanja(danas);
            licna.setDatumVazenja(danas.plusYears(10));
            licna.setRegBroj(UUID.randomUUID().toString());

            return licnaKartaRepository.save(licna);
        }

        Optional<LicnaKarta> odbijeniZahtevOpt = licnaKartaRepository.findByKorisnikAndStatus(korisnik, StatusZahteva.ODBIJEN);

        if (odbijeniZahtevOpt.isPresent()) {
            LicnaKarta novaLicna = new LicnaKarta();
            novaLicna.setKorisnik(korisnik);
            novaLicna.setDatumIzdavanja(LocalDate.now());
            novaLicna.setDatumVazenja(LocalDate.now().plusYears(10));
            novaLicna.setStatus(StatusZahteva.CEKANJE);
            novaLicna.setRegBroj(UUID.randomUUID().toString());

            return licnaKartaRepository.save(novaLicna);
        }

        throw new IllegalStateException("Korisnik nema aktivnu ličnu kartu niti odbijen zahtev.");
    }

    public List<LicnaKarta> prikaziSveZahteve(String authorizationHeader) {
        if (authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        }

        String email = jwtService.extractEmail(authorizationHeader);

        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        if (korisnik.getRola() == Role.EMPLOYER) {
            return licnaKartaRepository.findAll();
        }

        return licnaKartaRepository.findAllByKorisnik(korisnik);
    }

    public LicnaKarta odobriZahtev(Long licnaId) {
        LicnaKarta licna = licnaKartaRepository.findById(licnaId)
                .orElseThrow(() -> new IllegalArgumentException("Lična karta ne postoji"));

        if (licna.getStatus() != StatusZahteva.CEKANJE) {
            throw new IllegalStateException("Zahtev može biti odobren samo ako je u statusu CEKANJE!");
        }

        licna.setStatus(StatusZahteva.DOZVOLJEN);
        return licnaKartaRepository.save(licna);
    }

    public LicnaKarta odbijZahtev(Long licnaId) {
        LicnaKarta licna = licnaKartaRepository.findById(licnaId)
                .orElseThrow(() -> new IllegalArgumentException("Lična karta ne postoji"));

        if (licna.getStatus() != StatusZahteva.CEKANJE) {
            throw new IllegalStateException("Zahtev može biti odbijen samo ako je u statusu CEKANJE!");
        }

        licna.setStatus(StatusZahteva.ODBIJEN);
        return licnaKartaRepository.save(licna);
    }

    public LicnaKarta prijaviIzgubljeni(Long licnaId, String email) {
        LicnaKarta licna = licnaKartaRepository.findById(licnaId)
                .orElseThrow(() -> new IllegalArgumentException("Lična karta ne postoji"));

        if (!licna.getKorisnik().getEmail().equals(email)) {
            throw new IllegalArgumentException("Niste ovlašćeni za ovaj dokument");
        }

        if (licna.getStatus() != StatusZahteva.DOZVOLJEN) {
            throw new IllegalStateException("Samo aktivni dokumenti mogu biti prijavljeni kao izgubljeni");
        }

        licna.setStatus(StatusZahteva.IZGUBLJEN);
        return licnaKartaRepository.save(licna);
    }

}
