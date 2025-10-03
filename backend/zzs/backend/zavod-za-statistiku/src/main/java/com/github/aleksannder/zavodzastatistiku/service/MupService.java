package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.client.MupClient;
import com.github.aleksannder.zavodzastatistiku.dto.Highlights;
import com.github.aleksannder.zavodzastatistiku.dto.gdp.GdpGrowthHighlight;
import com.github.aleksannder.zavodzastatistiku.model.User;
import com.github.aleksannder.zavodzastatistiku.model.population.PopulationStat;
import com.github.aleksannder.zavodzastatistiku.repository.UserRepository;
import com.github.aleksannder.zavodzastatistiku.repository.wage.WageStatRepository;
import com.github.aleksannder.zavodzastatistiku.service.gdp.GdpStatService;
import com.github.aleksannder.zavodzastatistiku.service.population.PopulationStatService;
import com.github.aleksannder.zavodzastatistiku.service.wage.WageStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MupService {

    private final MupClient mupClient;
    private final WageStatService wageStatService;
    private final PopulationStatService populationStatService;
    private final GdpStatService gdpStatService;
    private final UserRepository userRepository;

    public Highlights getHighlights(String userEmail) {
        int now = LocalDateTime.now().getYear();
        User u = userRepository.findByEmail(userEmail).orElseThrow();
        return new Highlights(
                mupClient.countIdentificationCards(),
                mupClient.countVehicleLicences(),
                mupClient.countDriversLicenses(),
                mupClient.countGunPermits(),
                gdpStatService.gdpGrowthFromLastYear(now, u.getRegion()),
                wageStatService.findRegionWithHighestGrowth(now),
                populationStatService.extremes()
        );
    }

}
