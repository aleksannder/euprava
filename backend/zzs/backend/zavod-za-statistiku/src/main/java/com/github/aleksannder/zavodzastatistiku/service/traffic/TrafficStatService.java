package com.github.aleksannder.zavodzastatistiku.service.traffic;

import com.github.aleksannder.zavodzastatistiku.dto.traffic.DangerousRegionDTO;
import com.github.aleksannder.zavodzastatistiku.dto.traffic.FatalitiesTrendDto;
import com.github.aleksannder.zavodzastatistiku.dto.traffic.TrafficSummaryDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.traffic.TrafficStat;
import com.github.aleksannder.zavodzastatistiku.repository.traffic.TrafficStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrafficStatService {

    private final TrafficStatRepository repo;

    public Page<TrafficStat> findAll(Pageable pageable) { return repo.findAll(pageable); }

    public TrafficStat findById(Long id) { return repo.findById(id).orElse(null); }

    public TrafficStat save(TrafficStat stat) { return repo.save(stat); }

    public void delete(Long id) { repo.deleteById(id); }

    public List<TrafficStat> getTrendByRegion(Region region) {
        return repo.findByRegionOrderByYearAsc(region);
    }

    public List<TrafficSummaryDto> getSummaryOfVehiclesAndAccidentsByYear() {
        return repo.sumVehiclesAndAccidentsByYear();
    }

    public List<DangerousRegionDTO> getTopDangerousRegions(int yearFrom) {
        return repo.topDangerousRegions(yearFrom);
    }

    public List<FatalitiesTrendDto> getFatalitiesTrend() {
        return repo.fatalitiesTrend();
    }
}
