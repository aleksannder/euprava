package com.github.aleksannder.zavodzastatistiku.repository;

import com.github.aleksannder.zavodzastatistiku.model.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
}
