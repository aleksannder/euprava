package com.github.aleksannder.zavodzastatistiku.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aleksannder.zavodzastatistiku.dto.datapoint.DataPointResponse;
import com.github.aleksannder.zavodzastatistiku.model.DataPoint;
import com.github.aleksannder.zavodzastatistiku.repository.DataPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataPointService {

    private final DataPointRepository dataPointRepository;
    private final ObjectMapper objectMapper;

    public List<DataPointResponse> getByDatasetVersion(Long datasetVersionId) {
        return dataPointRepository.findByDatasetVersionId(datasetVersionId).stream()
                .map(this::toResponse)
                .toList();
    }

    private DataPointResponse toResponse(DataPoint dp) {
        try {
            Map<String, Object> dims = objectMapper.readValue(dp.getDims(), new TypeReference<>() {});
            Map<String, Object> measures = objectMapper.readValue(dp.getMeasures(), new TypeReference<>() {});

            return new DataPointResponse(
                    dp.getId(),
                    dp.getIndicator().getId(),
                    dp.getDatasetVersion().getId(),
                    dims,
                    measures,
                    dp.getCreatedAt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse datapoint JSON", e);
        }
    }
}
