package com.github.aleksannder.zavodzastatistiku.util;


import com.github.aleksannder.zavodzastatistiku.dto.indicator.IndicatorResponse;
import com.github.aleksannder.zavodzastatistiku.model.Indicator;

public class IndicatorConverter {

    public static IndicatorResponse toResponse(Indicator i) {
        return new IndicatorResponse(i.getId(), i.getCode(), i.getName(), i.getDescription(), i.getSubdomain().getId());
    }
}
