package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.dto.measure.MeasureRequest;
import com.github.aleksannder.zavodzastatistiku.model.Indicator;
import com.github.aleksannder.zavodzastatistiku.model.Measure;
import com.github.aleksannder.zavodzastatistiku.model.Unit;
import com.github.aleksannder.zavodzastatistiku.repository.IndicatorRepository;
import com.github.aleksannder.zavodzastatistiku.repository.MeasureRepository;
import com.github.aleksannder.zavodzastatistiku.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasureService {

    private final MeasureRepository measureRepository;
    private final IndicatorRepository indicatorRepository;
    private final UnitRepository unitRepository;

    public Measure create(MeasureRequest req) {
        Indicator i = indicatorRepository.findById(req.indicatorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid indicator id " + req.indicatorId()));
        Unit u = req.unitId() != null
                ? unitRepository.findById(req.unitId()).orElse(null)
                : null;
        Measure m = Measure.builder()
                .name(req.name())
                .agg(req.agg())
                .indicator(i)
                .unit(u)
                .build();

        return measureRepository.save(m);
    }
    public List<Measure> getAll() {
        return measureRepository.findAll();
    }

    public Measure getById(Long id) {
        return measureRepository.findById(id).orElse(null);
    }

    public Measure update(Long id, MeasureRequest req) {
        Measure existing = measureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Measure not found"));

        existing.setName(req.name());
        existing.setAgg(req.agg());
        existing.setIndicator(indicatorRepository.findById(req.indicatorId())
                .orElseThrow(() -> new IllegalArgumentException("Indicator not found")));
        if (req.unitId() != null) {
            existing.setUnit(unitRepository.findById(req.unitId()).orElse(null));
        }
        return measureRepository.save(existing);
    }

    public void delete(Long id) {
        measureRepository.deleteById(id);
    }

}
