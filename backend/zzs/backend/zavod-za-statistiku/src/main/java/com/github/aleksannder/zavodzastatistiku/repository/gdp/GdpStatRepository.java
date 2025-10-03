package com.github.aleksannder.zavodzastatistiku.repository.gdp;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.gdp.GdpStat;
import com.github.aleksannder.zavodzastatistiku.model.population.PopulationStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GdpStatRepository extends JpaRepository<GdpStat, Long> {

    List<GdpStat> findAllByOrderByYearAsc();

    @Query("""
           SELECT g FROM GdpStat g WHERE g.year = :year AND g.region = :region ORDER BY g.year DESC
        """)
    Optional<GdpStat> findByRegionAndYear(Region region, int year);

    GdpStat findTopByRegionOrderByYearDesc(Region region);

    @Query("SELECT g FROM GdpStat g WHERE g.cpiPercent IS NOT NULL ORDER BY g.year DESC")
    List<GdpStat> findAllWithCpi();

    @Query("""
        SELECT g 
        FROM GdpStat g 
        WHERE g.year IN (:currentYear, :previousYear)
        ORDER BY g.year
    """)
    List<GdpStat> findForGrowthComparison(@Param("currentYear") int currentYear,
                                          @Param("previousYear") int previousYear);

    Page<GdpStat> findAllByRegion(Region region, Pageable pageable);

    GdpStat findTopByRegionAndYearOrderByYearDesc(Region region, int year);
}
