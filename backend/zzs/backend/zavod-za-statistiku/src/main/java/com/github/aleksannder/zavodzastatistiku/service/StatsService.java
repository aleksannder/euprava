package com.github.aleksannder.zavodzastatistiku.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aleksannder.zavodzastatistiku.dto.HighlightResponse;
import com.github.aleksannder.zavodzastatistiku.model.DataPoint;
import com.github.aleksannder.zavodzastatistiku.repository.DataPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DataPointRepository dataPointRepository;
    private final ObjectMapper objectMapper;

    public List<HighlightResponse> getHighlights() {
        List<HighlightResponse> responses = new ArrayList<>();

        dataPointRepository.findTopByIndicator_CodeOrderByCreatedAtDesc("POPULATION")
                .ifPresent(dp -> responses.add(new HighlightResponse(
                        "Stanovništvo",
                        dp.getMeasures().getOrDefault("value", "?").toString(),
                        "groups",
                        dp.getIndicator().getSubdomain().getDomain().getCode(),
                        dp.getIndicator().getSubdomain().getCode(),
                        dp.getIndicator().getCode()
                )));

        dataPointRepository.findTopByIndicator_CodeOrderByCreatedAtDesc("GDP_GROWTH")
                .ifPresent(dp -> responses.add(new HighlightResponse(
                        "BDP",
                        dp.getMeasures().getOrDefault("value", "?").toString() + " % rast",
                        "show_chart",
                        dp.getIndicator().getSubdomain().getDomain().getCode(),
                        dp.getIndicator().getSubdomain().getCode(),
                        dp.getIndicator().getCode()
                )));

        dataPointRepository.findTopByIndicator_CodeOrderByCreatedAtDesc("INDUSTRY_GROWTH")
                .ifPresent(dp -> responses.add(new HighlightResponse(
                        "Industrija",
                        dp.getMeasures().getOrDefault("value", "?").toString() + " % rast",
                        "factory",
                        dp.getIndicator().getSubdomain().getDomain().getCode(),
                        dp.getIndicator().getSubdomain().getCode(),
                        dp.getIndicator().getCode()
                )));

        dataPointRepository.findTopByIndicator_CodeOrderByCreatedAtDesc("CPI_GROWTH")
                .ifPresent(dp -> responses.add(new HighlightResponse(
                        "Potrošačke cene",
                        dp.getMeasures().getOrDefault("value", "?").toString() + " % rast",
                        "sell",
                        dp.getIndicator().getSubdomain().getDomain().getCode(),
                        dp.getIndicator().getSubdomain().getCode(),
                        dp.getIndicator().getCode()
                )));

        dataPointRepository.findTopByIndicator_CodeOrderByCreatedAtDesc("NET_WAGE")
                .ifPresent(dp -> responses.add(new HighlightResponse(
                        "Neto zarada",
                        dp.getMeasures().getOrDefault("value", "?").toString() + " RSD",
                        "payments",
                        dp.getIndicator().getSubdomain().getDomain().getCode(),
                        dp.getIndicator().getSubdomain().getCode(),
                        dp.getIndicator().getCode()
                )));

        return responses;
    }
}
