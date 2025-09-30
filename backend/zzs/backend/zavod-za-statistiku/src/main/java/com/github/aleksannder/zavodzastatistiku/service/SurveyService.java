package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.model.survey.Survey;
import com.github.aleksannder.zavodzastatistiku.model.survey.SurveyResponse;
import com.github.aleksannder.zavodzastatistiku.repository.survey.SurveyRepository;
import com.github.aleksannder.zavodzastatistiku.repository.survey.SurveyResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;

    public List<Survey> findAll() {
        return surveyRepository.findAll();
    }

    public Survey findById(Long id) {
        return surveyRepository.findById(id).orElse(null);
    }

    public Survey save(Survey survey) {
        return surveyRepository.save(survey);
    }

    public void delete(Long id) {
        surveyRepository.deleteById(id);
    }

    public SurveyResponse saveResponse(SurveyResponse response) {
        return surveyResponseRepository.save(response);
    }

    public List<SurveyResponse> getResponsesForSurvey(Long surveyId) {
        return (List<SurveyResponse>) surveyResponseRepository.findAll(); // kasnije možemo filtrirati po surveyId
    }
}
