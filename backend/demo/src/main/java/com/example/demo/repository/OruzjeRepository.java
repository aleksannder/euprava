package com.example.demo.repository;

import com.example.demo.model.Oruzje;
import com.example.demo.model.StatusZahteva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OruzjeRepository extends JpaRepository<Oruzje, Long> {

    List<Oruzje> findAllByRegBroj(String regBroj);
    boolean existsByKorisnik_KorisnikIDAndKategorijaOruzjaContaining(Long korisnikID, String kategorija);
    boolean existsByKorisnik_KorisnikIDAndKategorijaOruzjaContainingAndStatusIn(
            Long korisnikId, String kategorija, List<StatusZahteva> statusi
    );
    List<Oruzje> findByKorisnik_KorisnikID(Long korisnikID);

}
