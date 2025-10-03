package com.github.aleksannder.zavodzastatistiku.repository.survey;

import com.github.aleksannder.zavodzastatistiku.model.survey.Survey;
import com.github.aleksannder.zavodzastatistiku.model.survey.SurveyResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SurveyResponseRepository extends CrudRepository<SurveyResponse, Long> {
    boolean existsBySurveyIdAndUserEmail(Long surveyId, String userEmail);

    List<SurveyResponse> findBySurveyId(Long surveyId);
}
