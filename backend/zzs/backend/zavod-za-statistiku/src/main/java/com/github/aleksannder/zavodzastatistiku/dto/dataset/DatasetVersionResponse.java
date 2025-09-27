package com.github.aleksannder.zavodzastatistiku.dto.dataset;

public record DatasetVersionResponse(
        Long id, String version, String createdAt, String publishedBy, Long datasetId
) {}
