package com.github.aleksannder.zavodzastatistiku.dto;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;

public record CitizenDashboardDto(
        Region region,
        Long population,
        Double averageAge,
        Double gdpBillion,
        Double wage,
        Long registeredVehicles,
        Long trafficAccidents
) {}
