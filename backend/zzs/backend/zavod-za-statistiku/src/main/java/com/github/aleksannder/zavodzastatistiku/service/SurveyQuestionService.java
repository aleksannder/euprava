package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.model.survey.SurveyDomain;
import com.github.aleksannder.zavodzastatistiku.model.survey.QuestionType;
import com.github.aleksannder.zavodzastatistiku.model.survey.Survey;
import com.github.aleksannder.zavodzastatistiku.model.survey.SurveyQuestion;

import java.util.ArrayList;
import java.util.List;


public class SurveyQuestionService {

    public List<SurveyQuestion> generateQuestionsForDomain(SurveyDomain domain, Survey survey) {
        List<SurveyQuestion> questions = new ArrayList<>();

        switch(domain) {
            case POPULATION -> {
                questions.add(new SurveyQuestion(null, "Koliko osoba živi u vašem domaćinstvu?", QuestionType.NUMBER, survey));
                questions.add(new SurveyQuestion(null, "Koji je prosečan broj dece u porodici?", QuestionType.NUMBER, survey));
            }
            case GDP -> {
                questions.add(new SurveyQuestion(null, "Kako ocenjujete ekonomske prilike u vašem regionu?", QuestionType.YES_NO, survey));
                questions.add(new SurveyQuestion(null, "Koliko ste povećali potrošnju u odnosu na prošlu godinu?", QuestionType.NUMBER, survey));
            }
            case WAGE -> {
                questions.add(new SurveyQuestion(null, "Kolika je vaša neto plata?", QuestionType.NUMBER, survey));
                questions.add(new SurveyQuestion(null, "Da li očekujete rast plata naredne godine?", QuestionType.YES_NO, survey));
            }
            case TRAFFIC -> {
                questions.add(new SurveyQuestion(null, "Koliko vozila vaša porodica poseduje?", QuestionType.NUMBER, survey));
                questions.add(new SurveyQuestion(null, "Da li ste bili učesnik saobraćajne nezgode u poslednjih godinu dana?", QuestionType.YES_NO, survey));
            }
        }

        return questions;
    }
}
