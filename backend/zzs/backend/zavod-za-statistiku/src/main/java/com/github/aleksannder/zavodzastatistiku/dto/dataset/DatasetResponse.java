package com.github.aleksannder.zavodzastatistiku.dto.dataset;

public record DatasetResponse(
        Long id, String name, String description, String status,
        boolean isPublic, String createdBy, String createdAt, Long indicatorId
) {}
