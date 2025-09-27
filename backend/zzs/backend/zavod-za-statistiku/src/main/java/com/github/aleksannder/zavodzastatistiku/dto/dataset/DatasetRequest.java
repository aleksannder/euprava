package com.github.aleksannder.zavodzastatistiku.dto.dataset;

public record DatasetRequest(
        String name, String description, boolean isPublic, Long indicatorId
) {}

