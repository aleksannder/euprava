package com.github.aleksannder.zavodzastatistiku.repository;

import com.github.aleksannder.zavodzastatistiku.model.Measure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasureRepository extends JpaRepository<Measure, Long> {
}
