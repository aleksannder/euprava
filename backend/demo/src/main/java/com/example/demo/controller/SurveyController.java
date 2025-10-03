package com.example.demo.controller;

import com.example.demo.client.ZzsClient;
import com.example.demo.dto.SurveyDto;
import com.example.demo.dto.zzs.SurveyResponse;
import com.example.demo.dto.zzs.SurveyWithStatus;
import com.example.demo.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @GetMapping("/with-status")
    public ResponseEntity<List<SurveyWithStatus>> getWithStatus(@RequestParam String userEmail) {
        return ResponseEntity.ok(surveyService.getSurveysWithStatus(userEmail));
    }

    @PostMapping("/{id}/responses")
    public ResponseEntity<?> submitResponses(@PathVariable String id, @RequestBody List<SurveyResponse> responses, @RequestParam String userEmail) {
        return surveyService.submitResponses(id, responses, userEmail);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDto> getSurvey(@PathVariable String id) {
        return surveyService.getSurveyById(id);
    }
}
