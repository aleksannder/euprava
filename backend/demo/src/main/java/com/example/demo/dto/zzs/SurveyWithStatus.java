package com.example.demo.dto.zzs;

import com.example.demo.dto.zzs.enums.SurveyDomain;

public record SurveyWithStatus(
        Long id, String title, SurveyDomain domain, boolean active, boolean responded
) {
}
