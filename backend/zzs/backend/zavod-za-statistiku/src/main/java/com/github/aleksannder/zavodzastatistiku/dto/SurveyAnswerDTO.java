package com.github.aleksannder.zavodzastatistiku.dto;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;

public record SurveyAnswerDTO(
        Long surveyId,
        String userEmail,
        Region region,
        Integer householdSize,
        Integer age,
        Integer childrenCount,
        Double wage,
        Boolean wageSatisfaction,
        Double monthlySpending,
        Integer livingStandard,
        Boolean ownsCar,
        Integer kmPerMonth,
        Boolean accidentLastYear
) {}
