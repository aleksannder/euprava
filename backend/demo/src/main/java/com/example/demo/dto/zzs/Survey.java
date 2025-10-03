package com.example.demo.dto.zzs;

import com.example.demo.dto.zzs.enums.SurveyDomain;

import java.util.List;

public record Survey(
        Long id, int year, SurveyDomain domain, boolean active, List<SurveyQuestion> questions
) {
}
