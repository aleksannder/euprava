package com.github.aleksannder.zavodzastatistiku.repository;

import com.github.aleksannder.zavodzastatistiku.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    Optional<Domain> getDomainById(Long id);

    @Query("SELECT d FROM Domain d LEFT JOIN FETCH d.subdomains WHERE d.id = :id")
    Domain getDomainByIdWithSubdomains(@Param("id") Long id);

    @Query("SELECT d FROM Domain d LEFT JOIN FETCH d.subdomains")
    List<Domain> findAllWithSubdomains();
}
