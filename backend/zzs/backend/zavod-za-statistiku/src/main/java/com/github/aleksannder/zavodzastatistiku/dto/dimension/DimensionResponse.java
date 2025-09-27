package com.github.aleksannder.zavodzastatistiku.dto.dimension;

import com.github.aleksannder.zavodzastatistiku.model.enums.DimensionType;

import java.util.List;

public record DimensionResponse(Long id, String name, DimensionType type, List<String > allowedValues) {}
