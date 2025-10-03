package com.example.demo.client;

import com.example.demo.dto.SurveyDto;
import com.example.demo.dto.UserSyncRequest;
import com.example.demo.dto.zzs.SurveyResponse;
import com.example.demo.dto.zzs.SurveyWithStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "zzs-service", url = "http://localhost:8080/api")
public interface ZzsClient {

    @GetMapping("/surveys/with-status")
    ResponseEntity<List<SurveyWithStatus>> getSurveysWithStatus(@RequestParam String userEmail);

    @PostMapping("/surveys/{surveyId}/responded/{userEmail}")
    Boolean hasUserResponded(@PathVariable String surveyId, @PathVariable String userEmail);

    @PostMapping("/surveys/{id}/responses")
    ResponseEntity<?> submitResponses(@PathVariable String id, @RequestBody List<SurveyResponse> responses);

    @PostMapping("/auth/update/statistics")
    Void sendUserDataToStatisticsService(@RequestBody UserSyncRequest korisnik);

    @GetMapping("/surveys/{surveyId}")
    ResponseEntity<SurveyDto> getSurvey(@PathVariable String surveyId);

}
