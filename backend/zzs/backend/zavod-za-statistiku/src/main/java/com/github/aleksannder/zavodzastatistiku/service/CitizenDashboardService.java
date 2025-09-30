package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.dto.CitizenDashboardDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.gdp.GdpStat;
import com.github.aleksannder.zavodzastatistiku.repository.gdp.GdpStatRepository;
import com.github.aleksannder.zavodzastatistiku.repository.population.PopulationStatRepository;
import com.github.aleksannder.zavodzastatistiku.repository.traffic.TrafficStatRepository;
import com.github.aleksannder.zavodzastatistiku.repository.wage.WageStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CitizenDashboardService {

    private final PopulationStatRepository populationStatRepository;
    private final GdpStatRepository gdpStatRepository;
    private final WageStatRepository wageStatRepository;
    private final TrafficStatRepository trafficStatRepository;

    public CitizenDashboardDto getDashboardForRegion(Region region) {
        var population = populationStatRepository.findTopByRegionOrderByYearDesc(region);
        var gdp = gdpStatRepository.findTopByRegionOrderByYearDesc(region);
        var wage = wageStatRepository.findTopByRegionOrderByYearDesc(region);
        var traffic = trafficStatRepository.findTopByRegionOrderByYearDesc(region);

        return new CitizenDashboardDto(
                region,
                population != null ? population.getPopulation() : null,
                population != null ? population.getAverageAge() : null,
                gdp != null ? gdp.getGdpBillion() : null,
                wage != null ? wage.getAverageWage(): null,
                traffic != null ? traffic.getRegisteredVehicles() : null,
                traffic != null ? traffic.getTrafficAccidents() : null
        );
    }
}
