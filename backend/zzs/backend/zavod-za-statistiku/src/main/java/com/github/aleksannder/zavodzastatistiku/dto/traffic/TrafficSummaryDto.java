package com.github.aleksannder.zavodzastatistiku.dto.traffic;

public record TrafficSummaryDto(
        int year, long totalVehicles, long totalAccidents
) {}
