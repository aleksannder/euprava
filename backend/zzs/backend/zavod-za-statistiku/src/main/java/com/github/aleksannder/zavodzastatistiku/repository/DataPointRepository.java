package com.github.aleksannder.zavodzastatistiku.repository;

import com.github.aleksannder.zavodzastatistiku.model.DataPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataPointRepository extends JpaRepository<DataPoint, Long> {

    List<DataPoint> findByDatasetVersionId(Long datasetVersionId);
}
