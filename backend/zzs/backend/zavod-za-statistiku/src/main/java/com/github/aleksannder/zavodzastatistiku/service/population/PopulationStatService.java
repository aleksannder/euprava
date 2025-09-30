package com.github.aleksannder.zavodzastatistiku.service.population;

import com.github.aleksannder.zavodzastatistiku.dto.AvgAgeDto;
import com.github.aleksannder.zavodzastatistiku.dto.population.ExtremesDto;
import com.github.aleksannder.zavodzastatistiku.dto.population.ProjectionDto;
import com.github.aleksannder.zavodzastatistiku.dto.population.RegionValueDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.population.PopulationStat;
import com.github.aleksannder.zavodzastatistiku.repository.population.PopulationStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopulationStatService {
    private final PopulationStatRepository repository;

    public List<PopulationStat> findAll() {
        return repository.findAll();
    }

    public PopulationStat findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public PopulationStat save(PopulationStat stat) {
        return repository.save(stat);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // use cases

    public List<PopulationStat> getStatsForRegion(Region region) {
        return repository.findByRegionOrderByYearAsc(region);
    }

    public List<PopulationStat> compareRegions(int year) {
        return repository.findByYear(year);
    }

    public List<RegionValueDto> avgAgeByRegion() {
        return repository.avgAgeByRegion().stream()
                .map(r -> new RegionValueDto((Region) r[0], ((Double) r[1])))
                .toList();
    }

    public List<RegionValueDto> naturalGrowth() {
        return repository.naturalGrowth().stream()
                .map(r -> new RegionValueDto((Region) r[0], ((Double) r[1])))
                .toList();
    }

    public ExtremesDto extremes() {
        List<Object[]> list = repository.avgAgeAll();
        Region youngest = null;
        Region oldest = null;
        double youngestAge = Double.MAX_VALUE;
        double oldestAge = Double.MIN_VALUE;

        for (Object[] row : list) {
            Region region = (Region) row[0];
            Double avgAge = (Double) row[1];
            if (avgAge != null) {
                if (avgAge < youngestAge) {
                    youngest = region;
                    youngestAge = avgAge;
                }
                if (avgAge > oldestAge) {
                    oldest = region;
                    oldestAge = avgAge;
                }
            }
        }

        return new ExtremesDto(youngest, youngestAge, oldest, oldestAge);
    }

    public List<ProjectionDto> projection(Region region) {
        List<PopulationStat> stats = repository.findByRegionOrderByYearAsc(region);

        if (stats.size() < 2) return Collections.emptyList();

        long diff = stats.get(stats.size() - 1).getPopulation() - stats.get(0).getPopulation();
        int years = stats.get(stats.size() - 1).getYear() - stats.get(0).getYear();
        long avgGrowth = diff / years;

        int lastYear = stats.get(stats.size() - 1).getYear();
        long lastPop = stats.get(stats.size() - 1).getPopulation();

        List<ProjectionDto> projections = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            projections.add(new ProjectionDto(lastYear + i, lastPop + avgGrowth * i));
        }
        return projections;
    }

    public List<RegionValueDto> populationShare(int year) {
        List<PopulationStat> stats = repository.findAll().stream()
                .filter(s -> s.getYear() == year)
                .toList();

        long total = stats.stream().mapToLong(PopulationStat::getPopulation).sum();

        return stats.stream()
                .map(s -> new RegionValueDto(s.getRegion(), (s.getPopulation() * 100.0) / total))
                .collect(Collectors.toList());
    }
}
