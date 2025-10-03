package com.github.aleksannder.zavodzastatistiku.dto.indicator;

public record IndicatorResponse(
        Long id, String code, String name, String description, Long subdomainId
) {}
