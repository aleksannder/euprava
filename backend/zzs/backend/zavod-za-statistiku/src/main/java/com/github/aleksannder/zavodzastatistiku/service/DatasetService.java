package com.github.aleksannder.zavodzastatistiku.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aleksannder.zavodzastatistiku.dto.dataset.DatasetRequest;
import com.github.aleksannder.zavodzastatistiku.dto.dataset.DatasetResponse;
import com.github.aleksannder.zavodzastatistiku.model.Dataset;
import com.github.aleksannder.zavodzastatistiku.model.Indicator;
import com.github.aleksannder.zavodzastatistiku.model.enums.Status;
import com.github.aleksannder.zavodzastatistiku.repository.DataPointRepository;
import com.github.aleksannder.zavodzastatistiku.repository.DatasetRepository;
import com.github.aleksannder.zavodzastatistiku.repository.IndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatasetService {

    private final DatasetRepository datasetRepository;
    private final IndicatorRepository indicatorRepository;

    public DatasetResponse create(DatasetRequest req) {
        Indicator indicator = null;
        if (req.indicatorId() != null) {
            indicator = indicatorRepository.findById(req.indicatorId())
                    .orElseThrow(() -> new IllegalArgumentException("Indicator not found"));
        }

        Dataset dataset = Dataset.builder()
                .name(req.name())
                .description(req.description())
                .isPublic(req.isPublic())
                .status(Status.DRAFT)  // default dok se ne objavi
                .createdBy("system")   // TODO: replace with current user
                .indicator(indicator)
                .build();

        Dataset saved = datasetRepository.save(dataset);
        return toResponse(saved);
    }

    public List<DatasetResponse> getAll() {
        return datasetRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public DatasetResponse getById(Long id) {
        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found"));
        return toResponse(dataset);
    }

    public void delete(Long id) {
        datasetRepository.deleteById(id);
    }

    private DatasetResponse toResponse(Dataset dataset) {
        return new DatasetResponse(
                dataset.getId(),
                dataset.getName(),
                dataset.getDescription(),
                dataset.getStatus() != null ? dataset.getStatus().name() : null,
                dataset.isPublic(),
                dataset.getCreatedBy(),
                dataset.getCreatedAt() != null ? dataset.getCreatedAt().toString() : null,
                dataset.getIndicator() != null ? dataset.getIndicator().getId() : null
        );
    }
}
