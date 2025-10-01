package com.github.aleksannder.zavodzastatistiku.service.wage;

import com.github.aleksannder.zavodzastatistiku.dto.wage.WageStatDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.wage.WageStat;
import com.github.aleksannder.zavodzastatistiku.repository.wage.WageStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WageStatService {

    private final WageStatRepository wageStatRepository;

    public List<WageStat> findAll() {
        return wageStatRepository.findAll();
    }

    public WageStat findById(Long id) {
        return wageStatRepository.findById(id).orElse(null);
    }

    public WageStat save(WageStat stat) {
        return wageStatRepository.save(stat);
    }

    public void delete(Long id) {
        wageStatRepository.deleteById(id);
    }

    public List<WageStat> getTrendByRegion(Region region) {
        return wageStatRepository.findByRegionOrderByYearAsc(region);
    }

    public List<WageStat> getComparisonByYear(int year) {
        return wageStatRepository.findByYear(year);
    }

    public List<Object[]> getRegionsSortedByAverageWage() {
        return wageStatRepository.getRegionsSortedByAverageWage();
    }

    public WageStatDto findRegionWithHighestGrowth(int year) {
        List<WageStat> current = wageStatRepository.findAllByYear(year);
        List<WageStat> previous = wageStatRepository.findAllByYear(year - 1);

        WageStatDto best = null;

        for (WageStat cur : current) {
            for (WageStat prev : previous) {
                if (prev.getRegion() == cur.getRegion()) {
                    double growthPercent = ((cur.getAverageWage() - prev.getAverageWage()) / prev.getAverageWage()) * 100.0;
                    double growthEuro = cur.getAverageWage() - prev.getAverageWage();

                    if (best == null || growthPercent > best.growthPercent()) {
                        best = new WageStatDto(cur.getRegion(), growthPercent, growthEuro);
                    }
                }
            }
        }

        return best;
    }
}
