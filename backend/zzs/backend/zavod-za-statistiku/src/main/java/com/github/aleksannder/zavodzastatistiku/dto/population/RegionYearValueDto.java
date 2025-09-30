package com.github.aleksannder.zavodzastatistiku.dto.population;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;

public record RegionYearValueDto(
        Region region, Integer year, Long value
) {}
