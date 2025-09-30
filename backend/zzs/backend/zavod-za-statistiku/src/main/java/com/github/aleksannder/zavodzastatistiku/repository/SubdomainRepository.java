package com.github.aleksannder.zavodzastatistiku.repository;

import com.github.aleksannder.zavodzastatistiku.model.Subdomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubdomainRepository extends JpaRepository<Subdomain, Long> {

    Subdomain findByCode(String code);
}
