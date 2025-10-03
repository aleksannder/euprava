package com.example.demo.service;

import com.example.demo.model.Korisnik;
import com.example.demo.model.LicnaKarta;
import com.example.demo.model.Role;
import com.example.demo.model.StatusZahteva;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.repository.LicnaKartaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicnaKartaService {

    private final LicnaKartaRepository licnaKartaRepository;
    private final KorisnikRepository korisnikRepository;


    public LicnaKarta podnesiZahtevIzTokena(String token, String drzava) {

        Korisnik korisnik = korisnikRepository.findByEmail(token)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        List<LicnaKarta> licne = licnaKartaRepository.findAllByUser(korisnik);

        boolean postojiAktivanZahtev = licne.stream()
                .anyMatch(l -> l.getStatus() == StatusZahteva.CEKANJE || l.getStatus() == StatusZahteva.DOZVOLJEN);

        if (postojiAktivanZahtev) {
            return null;
        }

        LicnaKarta licnaKarta = new LicnaKarta();
        licnaKarta.setUser(korisnik);
        licnaKarta.setName(korisnik.getFirstName());
        licnaKarta.setSurname(korisnik.getLastName());
        licnaKarta.setJmbg(korisnik.getJmbg());
        licnaKarta.setDateOfBirth(korisnik.getDateOfBirth());
        licnaKarta.setGender(korisnik.getGender());
        licnaKarta.setCity(korisnik.getCity());
        licnaKarta.setState(drzava);
        licnaKarta.setStatus(StatusZahteva.CEKANJE);
        licnaKarta.setRegistrationNumber(UUID.randomUUID().toString());
        licnaKarta.setDateOfIssuing(LocalDate.now());
        licnaKarta.setValidUntil(LocalDate.now().plusYears(10));

        return licnaKartaRepository.save(licnaKarta);
    }

    public LicnaKarta produziLicnuKartu(String authorizationHeader) {

        Korisnik korisnik = korisnikRepository.findByEmail(authorizationHeader)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        Optional<LicnaKarta> aktivnaLicnaOpt = licnaKartaRepository.findByUserAndStatus(korisnik, StatusZahteva.DOZVOLJEN);

        if (aktivnaLicnaOpt.isPresent()) {
            LicnaKarta licna = aktivnaLicnaOpt.get();
            LocalDate danas = LocalDate.now();
            LocalDate datumIsteka = licna.getValidUntil();

            if (datumIsteka.isAfter(danas.plusMonths(1))) {
                throw new IllegalStateException("Lična karta još nije spremna za produženje (više od mesec dana do isteka).");
            }

            licna.setDateOfIssuing(danas);
            licna.setValidUntil(danas.plusYears(10));
            licna.setRegistrationNumber(UUID.randomUUID().toString());

            return licnaKartaRepository.save(licna);
        }

        Optional<LicnaKarta> odbijeniZahtevOpt = licnaKartaRepository.findByUserAndStatus(korisnik, StatusZahteva.ODBIJEN);

        if (odbijeniZahtevOpt.isPresent()) {
            LicnaKarta novaLicna = new LicnaKarta();
            novaLicna.setUser(korisnik);
            novaLicna.setDateOfIssuing(LocalDate.now());
            novaLicna.setValidUntil(LocalDate.now().plusYears(10));
            novaLicna.setStatus(StatusZahteva.CEKANJE);
            novaLicna.setRegistrationNumber(UUID.randomUUID().toString());

            return licnaKartaRepository.save(novaLicna);
        }

        throw new IllegalStateException("Korisnik nema aktivnu ličnu kartu niti odbijen zahtev.");
    }

    public List<LicnaKarta> prikaziSveZahteve(String authorizationHeader) {

        Korisnik korisnik = korisnikRepository.findByEmail(authorizationHeader)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

        if (korisnik.getRole() == Role.EMPLOYER) {
            return licnaKartaRepository.findAll();
        }

        return licnaKartaRepository.findAllByUser(korisnik);
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

        if (!licna.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("Niste ovlašćeni za ovaj dokument");
        }

        if (licna.getStatus() != StatusZahteva.DOZVOLJEN) {
            throw new IllegalStateException("Samo aktivni dokumenti mogu biti prijavljeni kao izgubljeni");
        }

        licna.setStatus(StatusZahteva.IZGUBLJEN);
        return licnaKartaRepository.save(licna);
    }

}
