package com.github.aleksannder.zavodzastatistiku.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aleksannder.zavodzastatistiku.dto.dimension.DimensionRequest;
import com.github.aleksannder.zavodzastatistiku.model.Dimension;
import com.github.aleksannder.zavodzastatistiku.repository.DimensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DimensionService {
    private final DimensionRepository dimensionRepository;
    private final ObjectMapper objectMapper;

    public Dimension create(DimensionRequest req) {
        Dimension d = Dimension.builder()
                .name(req.name())
                .type(req.type())
                .build();
        try {
            d.setAllowedValues(objectMapper.writeValueAsString(req.allowedValues()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dimensionRepository.save(d);
    }

    public List<Dimension> getAll() {
        return dimensionRepository.findAll();
    }

    public Dimension getById(Long id) {
        return dimensionRepository.findById(id).orElse(null);
    }

    public Dimension update(Long id, DimensionRequest req) {
        Dimension existing = dimensionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Dimension with id " + id + " does not exist")
        );
        existing.setName(req.name());
        existing.setType(req.type());
        try {
            existing.setAllowedValues(objectMapper.writeValueAsString(req.allowedValues()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dimensionRepository.save(existing);
    }

    public void delete(Long id) {
        dimensionRepository.deleteById(id);
    }
}
