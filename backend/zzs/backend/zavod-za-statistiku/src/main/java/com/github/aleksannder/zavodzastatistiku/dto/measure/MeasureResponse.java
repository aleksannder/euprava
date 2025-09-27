package com.github.aleksannder.zavodzastatistiku.dto.measure;

public record MeasureResponse(
        Long id, String name, String agg, Long indicatorId, Long unitId
) {}
