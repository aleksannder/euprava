package com.github.aleksannder.zavodzastatistiku.service.survey;

import com.github.aleksannder.zavodzastatistiku.model.survey.Survey;
import com.github.aleksannder.zavodzastatistiku.model.survey.SurveyResponse;
import com.github.aleksannder.zavodzastatistiku.repository.survey.SurveyRepository;
import com.github.aleksannder.zavodzastatistiku.repository.survey.SurveyResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyResponseService {

    private final SurveyResponseRepository repository;
    private final SurveyRepository surveyRepository;

    public SurveyResponse submitResponse(Long surveyId, SurveyResponse request) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));

        request.setSurvey(survey);
        return repository.save(request);
    }

    public List<SurveyResponse> findBySurveyId(Long surveyId) {
        return repository.findBySurveyId(surveyId);
    }
}
