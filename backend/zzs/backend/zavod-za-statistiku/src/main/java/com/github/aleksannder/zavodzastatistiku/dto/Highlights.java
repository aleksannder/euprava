package com.github.aleksannder.zavodzastatistiku.dto;

import com.github.aleksannder.zavodzastatistiku.dto.gdp.GdpGrowthHighlight;
import com.github.aleksannder.zavodzastatistiku.dto.population.ExtremesDto;
import com.github.aleksannder.zavodzastatistiku.dto.wage.WageStatDto;

public record Highlights(
        long identificationCards, long vehicleLicences, long driversLicenses, long gunPermits,
        GdpGrowthHighlight gdpGrowth, WageStatDto highestPaidRegion, ExtremesDto extremesDto
) {
}
