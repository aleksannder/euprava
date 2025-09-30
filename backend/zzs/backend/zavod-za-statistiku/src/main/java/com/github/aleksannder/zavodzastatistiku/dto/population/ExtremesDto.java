package com.github.aleksannder.zavodzastatistiku.dto.population;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;

public record ExtremesDto(
        Region youngestRegion, Double youngestAge,
        Region oldestRegion, Double oldestAge
) {}
