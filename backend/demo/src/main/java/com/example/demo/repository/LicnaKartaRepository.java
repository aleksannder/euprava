package com.example.demo.repository;

import com.example.demo.model.Korisnik;
import com.example.demo.model.LicnaKarta;
import com.example.demo.model.StatusZahteva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LicnaKartaRepository extends JpaRepository<LicnaKarta, Long> {
    Optional<LicnaKarta> findByKorisnik(Korisnik korisnik);

    Optional<LicnaKarta> findByKorisnikAndStatus(Korisnik korisnik, StatusZahteva status);

    List<LicnaKarta> findAllByKorisnik(Korisnik korisnik);

}
