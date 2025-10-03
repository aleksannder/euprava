package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.dto.dataset.DatasetVersionRequest;
import com.github.aleksannder.zavodzastatistiku.dto.dataset.DatasetVersionResponse;
import com.github.aleksannder.zavodzastatistiku.model.Dataset;
import com.github.aleksannder.zavodzastatistiku.model.DatasetVersion;
import com.github.aleksannder.zavodzastatistiku.repository.DatasetRepository;
import com.github.aleksannder.zavodzastatistiku.repository.DatasetVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasetVersionService {

    private final DatasetVersionRepository datasetVersionRepository;
    private final DatasetRepository datasetRepository;

    public DatasetVersionResponse create(Long datasetId, DatasetVersionRequest req) {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found"));

        DatasetVersion version = DatasetVersion.builder()
                .dataset(dataset)
                .version(req.version())
                .publishedBy(req.publishedBy())
                .build();

        DatasetVersion saved = datasetVersionRepository.save(version);
        return toResponse(saved);
    }

    public DatasetVersionResponse getById(Long id) {
        DatasetVersion dv = datasetVersionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DatasetVersion not found"));
        return toResponse(dv);
    }

    public List<DatasetVersionResponse> getAllByDataset(Long datasetId) {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found"));

        return datasetVersionRepository.findByDataset(dataset).stream()
                .map(this::toResponse)
                .toList();
    }

    private DatasetVersionResponse toResponse(DatasetVersion dv) {
        return new DatasetVersionResponse(
                dv.getId(),
                dv.getVersion(),
                dv.getCreatedAt() != null ? dv.getCreatedAt().toString() : null,
                dv.getPublishedBy(),
                dv.getDataset() != null ? dv.getDataset().getId() : null
        );
    }
}
