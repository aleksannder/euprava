package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.model.survey.Survey;
import com.github.aleksannder.zavodzastatistiku.model.survey.SurveyResponse;
import com.github.aleksannder.zavodzastatistiku.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService service;

    @GetMapping
    public ResponseEntity<List<Survey>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getById(@PathVariable Long id) {
        Survey survey = service.findById(id);
        return survey == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(survey);
    }

    @PostMapping
    public ResponseEntity<Survey> create(@RequestBody Survey survey) {
        return ResponseEntity.ok(service.save(survey));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/responses")
    public ResponseEntity<SurveyResponse> submitResponse(
            @PathVariable Long id,
            @RequestBody SurveyResponse response
    ) {
        return ResponseEntity.ok(service.saveResponse(response));
    }

    @GetMapping("/{id}/responses")
    public ResponseEntity<List<SurveyResponse>> getResponses(@PathVariable Long id) {
        return ResponseEntity.ok(service.getResponsesForSurvey(id));
    }
}
