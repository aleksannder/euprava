package com.github.aleksannder.zavodzastatistiku.dto.survey;

import com.github.aleksannder.zavodzastatistiku.model.survey.SurveyDomain;

public record SurveyWithStatusDto(
        Long id, String title, SurveyDomain domain, boolean active, boolean responded
) {
}
