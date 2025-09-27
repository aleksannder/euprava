package com.github.aleksannder.zavodzastatistiku.dto.indicator;

public record IndicatorRequest(String code, String name,
                               String description, Long subdomainId) {}
