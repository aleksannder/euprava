package com.github.aleksannder.zavodzastatistiku.repository.wage;


import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.population.PopulationStat;
import com.github.aleksannder.zavodzastatistiku.model.wage.WageStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WageStatRepository extends JpaRepository<WageStat, Long> {

    List<WageStat> findByRegionOrderByYearAsc(Region region);

    List<WageStat> findByYear(int year);

    WageStat findTopByRegionOrderByYearDesc(Region region);

    @Query("""
        SELECT w.region, AVG(w.averageWage) 
        FROM WageStat w 
        GROUP BY w.region 
        ORDER BY AVG(w.averageWage) DESC 
            """)
    List<Object[]> getRegionsSortedByAverageWage();

    @Query("""
        SELECT w FROM WageStat w WHERE w.region = :region AND w.year = :year
        """)
    Optional<WageStat> findByRegionAndYear(@Param("region") Region region, @Param("year") int year);

    @Query("""
           SELECT w FROM WageStat w WHERE w.year = :year
        """)
    List<WageStat> findAllByYear(@Param("year") int year);
}