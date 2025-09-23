package com.example.demo.repository;

import com.example.demo.model.Korisnik;
import com.example.demo.model.VozackaDozvola;
import com.example.demo.model.StatusZahteva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VozackaDozvolaRepository extends JpaRepository<VozackaDozvola, Long> {

    List<VozackaDozvola> findAllByKorisnik(Korisnik korisnik);

    Optional<VozackaDozvola> findByKorisnikAndStatus(Korisnik korisnik, StatusZahteva status);

}
