package com.example.demo.service;

import com.example.demo.dto.OruzjeRequest;
import com.example.demo.model.Korisnik;
import com.example.demo.model.Oruzje;
import com.example.demo.model.Role;
import com.example.demo.model.StatusZahteva;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.repository.OruzjeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class OruzjeService {

    private final OruzjeRepository repository;
    private final KorisnikRepository korisnikRepository;

    public OruzjeService(OruzjeRepository repository, KorisnikRepository korisnikRepository) {
        this.repository = repository;
        this.korisnikRepository = korisnikRepository;
    }

    public Oruzje podnesiZahtev(String email, OruzjeRequest request) {
        Korisnik korisnik = korisnikRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen"));

        for (String kategorija : request.getKategorijaOruzja()) {
            boolean postojiAktivan = repository.existsByKorisnik_KorisnikIDAndKategorijaOruzjaContainingAndStatusIn(
                    korisnik.getKorisnikID(),
                    kategorija,
                    List.of(StatusZahteva.CEKANJE, StatusZahteva.DOZVOLJEN)
            );
            if (postojiAktivan) {
                throw new IllegalArgumentException("Već postoji aktivan zahtev za kategoriju: " + kategorija);
            }
        }

        String noveKategorije = String.join(",", request.getKategorijaOruzja());
        String ime = korisnik.getIme();
        String prezime = korisnik.getPrezime();
        String regBroj = generisiRegBroj();
        LocalDate datumDo = request.getDatumOd().plusYears(10);

        Oruzje o = Oruzje.kreiraj(
                ime,
                prezime,
                request.getDatumOd(),
                datumDo,
                regBroj,
                noveKategorije,
                korisnik
        );

        return repository.save(o);
    }

    private String generisiRegBroj() {
        Random random = new Random();
        int broj = 100000 + random.nextInt(900000);
        return "OR-" + broj;
    }

    public List<Oruzje> sviZahtevi() {
        return repository.findAll();
    }

    public Oruzje odobriZahtev(Long id) {
        Oruzje o = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Zahtev ne postoji"));
        o.setStatus(StatusZahteva.DOZVOLJEN);
        return repository.save(o);
    }

    public Oruzje odbijZahtev(Long id) {
        Oruzje o = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Zahtev ne postoji"));
        o.setStatus(StatusZahteva.ODBIJEN);
        return repository.save(o);
    }

    public int produziSveZahteveKojiIsticuZaMesec() {
        LocalDate danas = LocalDate.now();
        LocalDate threshold = danas.plusDays(30);

        List<Oruzje> zahteviZaProduzenje = repository.findAll().stream()
                .filter(z -> z.getStatus() == StatusZahteva.DOZVOLJEN)
                .filter(z -> !z.getDatumDo().isAfter(threshold))
                .toList();

        for (Oruzje o : zahteviZaProduzenje) {
            o.setDatumDo(o.getDatumDo().plusYears(10));
            repository.save(o);
        }

        return zahteviZaProduzenje.size();
    }

    public List<Oruzje> sviZahteviPoKorisnikuIliSvi(Role role, String email) {
        if (role == Role.EMPLOYER) {
            return repository.findAll();
        } else {
            Korisnik korisnik = korisnikRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronađen"));
            return repository.findByKorisnik_KorisnikID(korisnik.getKorisnikID());
        }
    }

    public Oruzje prijaviIzgubljeni(Long oruzjeId, String email) {
        Oruzje o = repository.findById(oruzjeId)
                .orElseThrow(() -> new IllegalArgumentException("Oružje ne postoji"));

        if (!o.getKorisnik().getEmail().equals(email)) {
            throw new IllegalArgumentException("Nije vaš dokument");
        }

        if (o.getStatus() != StatusZahteva.DOZVOLJEN) {
            throw new IllegalStateException("Dokument nije aktivan");
        }

        o.setStatus(StatusZahteva.IZGUBLJEN);
        return repository.save(o);
    }

}
