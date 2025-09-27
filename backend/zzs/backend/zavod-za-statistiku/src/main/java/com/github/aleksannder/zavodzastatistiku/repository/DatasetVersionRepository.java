package com.github.aleksannder.zavodzastatistiku.repository;

import com.github.aleksannder.zavodzastatistiku.model.Dataset;
import com.github.aleksannder.zavodzastatistiku.model.DatasetVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatasetVersionRepository extends JpaRepository<DatasetVersion, Long> {

    List<DatasetVersion> findByDataset(Dataset dataset);
}
