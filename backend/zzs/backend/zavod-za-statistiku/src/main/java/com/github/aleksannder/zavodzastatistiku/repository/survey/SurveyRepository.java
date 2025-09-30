package com.github.aleksannder.zavodzastatistiku.repository.survey;

import com.github.aleksannder.zavodzastatistiku.model.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
