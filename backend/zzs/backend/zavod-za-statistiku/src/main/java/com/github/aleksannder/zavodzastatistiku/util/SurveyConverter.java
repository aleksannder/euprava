package com.github.aleksannder.zavodzastatistiku.util;


import com.github.aleksannder.zavodzastatistiku.dto.survey.SurveyDto;
import com.github.aleksannder.zavodzastatistiku.dto.survey.SurveyQuestionDto;
import com.github.aleksannder.zavodzastatistiku.model.survey.Survey;

public class SurveyConverter {

    public static SurveyDto toDto(Survey survey) {
        return new SurveyDto(
                survey.getId(),
                survey.getTitle(),
                survey.getDomain().name(),
                survey.getYear(),
                survey.isActive(),
                survey.getQuestions().stream()
                        .map(q -> new SurveyQuestionDto(q.getId(), q.getText(), q.getType().name()))
                        .toList()
        );
    }
}
