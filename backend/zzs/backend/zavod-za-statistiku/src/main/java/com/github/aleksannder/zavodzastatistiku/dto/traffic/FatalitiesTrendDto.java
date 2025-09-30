package com.github.aleksannder.zavodzastatistiku.dto.traffic;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;

public record FatalitiesTrendDto(
        int year, Region region, long fatalities
) {
}
