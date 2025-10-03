package com.github.aleksannder.zavodzastatistiku.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aleksannder.zavodzastatistiku.dto.dimension.DimensionResponse;
import com.github.aleksannder.zavodzastatistiku.model.Dimension;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DimensionConverter {

    private final ObjectMapper objectMapper;

    public DimensionResponse toResponse(Dimension d) {
        try {
            List<String> allowedValues = d.getAllowedValues() == null
                    ? List.of()
                    : objectMapper.readValue(d.getAllowedValues(), new TypeReference<>() {});
            return new DimensionResponse(d.getId(), d.getName(), d.getType(), allowedValues);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
