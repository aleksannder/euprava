package com.github.aleksannder.zavodzastatistiku.service.gdp;

import com.github.aleksannder.zavodzastatistiku.dto.GdpGrowthHighlight;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.gdp.GdpStat;
import com.github.aleksannder.zavodzastatistiku.repository.gdp.GdpStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GdpStatService {

    private final GdpStatRepository gdpStatRepository;

    public Page<GdpStat> findAll(Pageable pageable) {
        return gdpStatRepository.findAll(pageable);
    }

    public GdpStat findById(Long id) {
        return gdpStatRepository.findById(id).orElse(null);
    }

    public GdpStat save(GdpStat stat) {
        return gdpStatRepository.save(stat);
    }

    public void delete(Long id) {
        gdpStatRepository.deleteById(id);
    }

    public List<GdpStat> getTrend() {
        return gdpStatRepository.findAllByOrderByYearAsc();
    }

    public List<GdpStat> getCpi() {
        return gdpStatRepository.findAllWithCpi();
    }

    public GdpGrowthHighlight gdpGrowthFromLastYear(int year, Region region) {
        List<GdpStat> stats = gdpStatRepository.findForGrowthComparison(year, year - 1);

        if (stats.size() < 2) {
            return null;
        }

        GdpStat prev = stats.stream()
                .filter(s -> s.getYear() == year - 1 && s.getRegion() == region)
                .findFirst().orElse(null);

        GdpStat curr = stats.stream()
                .filter(s -> s.getYear() == year && s.getRegion() == region)
                .findFirst().orElse(null);

        if (prev == null || curr == null) return null;

        double absGrowth = curr.getGdpBillion() - prev.getGdpBillion();
        double percentGrowth = (absGrowth / prev.getGdpBillion()) * 100.0;

        return new GdpGrowthHighlight(region, year, percentGrowth, absGrowth);
    }

    public Page<GdpStat> getAllStatsForRegion(Region region, Pageable pageable) {
            return gdpStatRepository.findAllByRegion(region, pageable);
    }
}
