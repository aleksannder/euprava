package com.example.demo.dto.zzs;


import com.example.demo.model.Region;

public record SurveyResponse(
        Long id, String userEmail, Region region, Survey survey, SurveyQuestion surveyQuestion, String answer
) {
}
