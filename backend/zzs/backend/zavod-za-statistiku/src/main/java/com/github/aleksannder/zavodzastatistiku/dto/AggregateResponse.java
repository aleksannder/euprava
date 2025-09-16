package com.github.aleksannder.zavodzastatistiku.dto;

import java.util.List;
import java.util.Map;

public record AggregateResponse(
        String dataset,
        List<Map<String, Object>> rows) {}
