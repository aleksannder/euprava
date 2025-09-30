package com.github.aleksannder.zavodzastatistiku.repository.survey;

import com.github.aleksannder.zavodzastatistiku.model.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    List<Survey> findByActiveTrue();
}
