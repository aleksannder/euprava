package com.example.demo.repository;

import com.example.demo.model.Korisnik;
import com.example.demo.model.SaobracajnaDozvola;
import com.example.demo.model.StatusZahteva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaobracajnaDozvolaRepository extends JpaRepository<SaobracajnaDozvola, Long> {

    List<SaobracajnaDozvola> findAllByUser(Korisnik korisnik);
    Optional<SaobracajnaDozvola> findByUserAndStatus(Korisnik korisnik, StatusZahteva status);
    Long countVehicleLicencesByStatusEquals(StatusZahteva status);
}
