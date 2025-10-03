package com.github.aleksannder.zavodzastatistiku.dto.wage;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;

public record WageStatDto(
        Region region, double growthPercent, double growthRsd
) {}
