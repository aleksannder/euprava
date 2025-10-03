package com.example.demo.dto.zzs;

import com.example.demo.dto.zzs.enums.QuestionType;

public record SurveyQuestion(
        Long id, String text, QuestionType type, Survey survey
) {
}
