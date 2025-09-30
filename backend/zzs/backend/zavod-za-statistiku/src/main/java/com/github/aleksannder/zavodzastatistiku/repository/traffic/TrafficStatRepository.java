package com.github.aleksannder.zavodzastatistiku.repository.traffic;


import com.github.aleksannder.zavodzastatistiku.dto.traffic.DangerousRegionDTO;
import com.github.aleksannder.zavodzastatistiku.dto.traffic.FatalitiesTrendDto;
import com.github.aleksannder.zavodzastatistiku.dto.traffic.TrafficSummaryDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.population.PopulationStat;
import com.github.aleksannder.zavodzastatistiku.model.traffic.TrafficStat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrafficStatRepository extends JpaRepository<TrafficStat, Long> {

    List<TrafficStat> findByRegionOrderByYearAsc(Region region);

    TrafficStat findTopByRegionOrderByYearDesc(Region region);

    @Query("""
    SELECT t.region, SUM(t.fatalities) as totalFatalities
    FROM TrafficStat t
    GROUP BY t.region
    ORDER BY totalFatalities ASC
""")
    List<Object[]> findSafestRegion();

    @Query("""
    SELECT t.region, AVG(CAST(t.trafficAccidents AS double)) / AVG(CAST(t.registeredVehicles AS double))
    FROM TrafficStat t
    GROUP BY t.region
    ORDER BY (AVG(CAST(t.trafficAccidents AS double)) / AVG(CAST(t.registeredVehicles AS double))) ASC
""")
    List<Object[]> calculateRiskIndex();

    @Query("""
            SELECT new com.github.aleksannder.zavodzastatistiku.dto.traffic.TrafficSummaryDto(
                    t.year,
                            SUM(t.registeredVehicles),
                                    SUM(t.trafficAccidents)
                    )
                            FROM TrafficStat t
                                    GROUP BY t.year ORDER BY t.year
        
        """)
    List<TrafficSummaryDto> sumVehiclesAndAccidentsByYear();

    @Query("""
           SELECT new com.github.aleksannder.zavodzastatistiku.dto.traffic.DangerousRegionDTO(
                      t.region,
                                 SUM(t.trafficAccidents)
                      )
                                 FROM TrafficStat t WHERE t.year >= :yearFrom GROUP BY t.region ORDER BY AVG(t.trafficAccidents) DESC
           """)
    List<DangerousRegionDTO> topDangerousRegions(@Param("yearFrom") int yearFrom);

    @Query("""
           SELECT new com.github.aleksannder.zavodzastatistiku.dto.traffic.FatalitiesTrendDto(
                      t.year,
                                 t.region,
                                            SUM(t.fatalities)
                      )
                                 FROM TrafficStat t GROUP BY t.year, t.region ORDER BY t.year ASC
           """)
    List<FatalitiesTrendDto> fatalitiesTrend();
}
