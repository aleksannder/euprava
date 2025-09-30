package com.github.aleksannder.zavodzastatistiku.dto;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;

public record AvgAgeDto(
        Region region, String regionLabel, Double averageAge
) {}
