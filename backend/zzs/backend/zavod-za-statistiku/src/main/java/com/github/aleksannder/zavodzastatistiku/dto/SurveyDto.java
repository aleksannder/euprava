package com.github.aleksannder.zavodzastatistiku.dto;

import java.util.List;

public record SurveyDto(Long id, String title, String domain, int year, boolean active, List<SurveyQuestionDto> questions) {}