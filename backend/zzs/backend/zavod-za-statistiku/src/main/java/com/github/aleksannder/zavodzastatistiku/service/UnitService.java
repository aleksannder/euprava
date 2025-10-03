package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.dto.unit.UnitRequest;
import com.github.aleksannder.zavodzastatistiku.model.Unit;
import com.github.aleksannder.zavodzastatistiku.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService {
    private final UnitRepository unitRepository;

    public Unit create(UnitRequest unitRequest) {
        Unit u = Unit.builder()
                .code(unitRequest.code())
                .name(unitRequest.name())
                .build();
        return unitRepository.save(u);
    }

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public Unit getUnitById(Long unitId) {
        return unitRepository.findById(unitId).orElse(null);
    }

    public Unit updateUnit(UnitRequest unitRequest, Long id) {
        Unit existing = unitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unit not found"));
        existing.setCode(unitRequest.code());
        existing.setName(unitRequest.name());
        return unitRepository.save(existing);
    }

    public void deleteUnit(Long id) {
        unitRepository.deleteById(id);
    }
}
