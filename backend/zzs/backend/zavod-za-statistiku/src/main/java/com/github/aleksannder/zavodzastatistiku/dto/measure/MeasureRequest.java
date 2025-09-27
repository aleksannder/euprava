package com.github.aleksannder.zavodzastatistiku.dto.measure;

public record MeasureRequest(
        String name, String agg, Long indicatorId, Long unitId
) {}
