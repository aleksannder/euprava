package com.example.demo.repository;

import com.example.demo.model.Oruzje;
import com.example.demo.model.StatusZahteva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OruzjeRepository extends JpaRepository<Oruzje, Long> {

    List<Oruzje> findAllByRegistrationNumber(String regBroj);
    boolean existsByUser_IdAndGunCategoryContaining(Long korisnikID, String kategorija);
    boolean existsByUser_IdAndGunCategoryContainingAndStatusIn(
            Long korisnikId, String kategorija, List<StatusZahteva> statusi);

    List<Oruzje> findByUser_Id(Long korisnikID);

    Long countOruzjeByStatusEquals(StatusZahteva status);
}
