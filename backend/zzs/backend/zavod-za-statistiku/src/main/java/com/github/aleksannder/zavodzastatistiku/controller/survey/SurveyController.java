package com.github.aleksannder.zavodzastatistiku.controller.survey;

import com.github.aleksannder.zavodzastatistiku.dto.survey.SurveyDto;
import com.github.aleksannder.zavodzastatistiku.dto.survey.SurveyWithStatusDto;
import com.github.aleksannder.zavodzastatistiku.model.survey.Survey;
import com.github.aleksannder.zavodzastatistiku.model.survey.SurveyResponse;
import com.github.aleksannder.zavodzastatistiku.repository.survey.SurveyResponseRepository;
import com.github.aleksannder.zavodzastatistiku.service.survey.SurveyService;
import com.github.aleksannder.zavodzastatistiku.util.SurveyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final SurveyResponseRepository surveyResponseRepository;

    @PreAuthorize("hasRole('ANALYST')")
    @PostMapping
    public ResponseEntity<SurveyDto> createSurvey(@RequestBody Survey survey) {
        Survey s = surveyService.createSurvey(survey);

        return ResponseEntity.ok(SurveyConverter.toDto(s));
    }

    @PreAuthorize("hasAnyRole('ANALYST', 'CITIZEN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<SurveyDto>> getAll() {
        return ResponseEntity.ok(surveyService.getAll());
    }

    @PreAuthorize("hasRole('CITIZEN')")
    @PostMapping("/{id}/responses")
    public ResponseEntity<?> submitResponses(@PathVariable Long id, @RequestBody List<SurveyResponse> responses) {
        if (surveyService.hasUserResponded(id, responses.get(0).getUserEmail())) {
            return ResponseEntity.badRequest().build();
        }
        surveyService.saveResponses(responses);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('CITIZEN', 'ANALYST', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SurveyDto> getSurvey(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getById(id));
    }

    @PreAuthorize("hasRole('ANALYST')")
    @PostMapping("/{id}/close")
    public ResponseEntity<Void> closeSurvey(@PathVariable Long id) {
        surveyService.closeSurvey(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CITIZEN')")
    @GetMapping("/{surveyId}/responded/{userEmail}")
    public ResponseEntity<Boolean> hasUserResponded(@PathVariable Long surveyId, @PathVariable String userEmail) {
        return ResponseEntity.ok(surveyService.hasUserResponded(surveyId, userEmail));
    }

    @PreAuthorize("hasAnyRole('CITIZEN', 'ANALYST')")
    @GetMapping("/with-status")
    public ResponseEntity<List<SurveyWithStatusDto>> getSurveysWithStatus(@RequestParam String userEmail) {
        List<Survey> surveys = surveyService.findAll();

        List<SurveyWithStatusDto> result = surveys.stream()
                .map(s -> new SurveyWithStatusDto(
                        s.getId(),
                        s.getTitle(),
                        s.getDomain(),
                        s.isActive(),
                        surveyResponseRepository.existsBySurveyIdAndUserEmail(s.getId(), userEmail)
                ))
                .toList();

        return ResponseEntity.ok(result);
    }
}
