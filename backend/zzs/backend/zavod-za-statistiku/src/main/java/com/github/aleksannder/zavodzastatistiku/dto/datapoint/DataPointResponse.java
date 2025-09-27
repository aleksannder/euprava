package com.github.aleksannder.zavodzastatistiku.dto.datapoint;

import java.time.Instant;
import java.util.Map;

public record DataPointResponse(
        Long id, Long indicatorId, Long datasetVersionId,
        Map<String, Object> dims, Map<String, Object> measures, Instant createdAt
) {}
