package com.github.aleksannder.zavodzastatistiku.util;

import com.github.aleksannder.zavodzastatistiku.dto.measure.MeasureResponse;
import com.github.aleksannder.zavodzastatistiku.model.Measure;

public class MeasureConverter {

    public static MeasureResponse toResponse(Measure measure) {
        return new MeasureResponse(
                measure.getId(),
                measure.getName(),
                measure.getAgg(),
                measure.getIndicator().getId(),
                measure.getUnit().getId()
        );
    }
}
