package com.github.aleksannder.zavodzastatistiku.dto;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;

public record GdpGrowthHighlight(
        Region region, int year, double growthPercent, double growthBillion
) {}
