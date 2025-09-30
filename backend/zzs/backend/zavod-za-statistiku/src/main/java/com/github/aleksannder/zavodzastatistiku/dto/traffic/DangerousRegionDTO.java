package com.github.aleksannder.zavodzastatistiku.dto.traffic;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;

public record DangerousRegionDTO(
        Region region, double avgAccidents
) {
}
