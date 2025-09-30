package com.github.aleksannder.zavodzastatistiku.repository.population;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.population.PopulationStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PopulationStatRepository extends JpaRepository<PopulationStat, Long> {

    List<PopulationStat> findByRegionOrderByYearAsc(Region region);

    List<PopulationStat> findByYear(int year);

    PopulationStat findTopByRegionOrderByYearDesc(Region region);

    @Query("SELECT p.region, AVG(p.averageAge) FROM PopulationStat p GROUP BY p.region")
    List<Object[]> avgAgeByRegion();

    @Query("SELECT p.region, AVG(p.birthRate - p.mortalityRate) FROM PopulationStat p GROUP BY p.region")
    List<Object[]> naturalGrowth();

    @Query("SELECT p.region, AVG(p.averageAge) FROM PopulationStat p GROUP BY p.region")
    List<Object[]> avgAgeAll();
}
