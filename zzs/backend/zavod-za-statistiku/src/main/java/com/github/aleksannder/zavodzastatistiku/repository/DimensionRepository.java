package com.github.aleksannder.zavodzastatistiku.repository;

import com.github.aleksannder.zavodzastatistiku.model.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DimensionRepository extends JpaRepository<Dimension, Long> {
}
