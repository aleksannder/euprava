package com.example.demo.service;

import com.example.demo.client.ZzsClient;
import com.example.demo.dto.SurveyDto;
import com.example.demo.dto.zzs.Survey;
import com.example.demo.dto.zzs.SurveyResponse;
import com.example.demo.dto.zzs.SurveyWithStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final ZzsClient zzsClient;

    public List<SurveyWithStatus> getSurveysWithStatus(String userEmail) {

        return zzsClient.getSurveysWithStatus(userEmail).getBody();
    }

    public ResponseEntity<?> submitResponses(String id, List<SurveyResponse> responses, String userEmail) {
        if (zzsClient.hasUserResponded(id, userEmail)) {
            return ResponseEntity.badRequest().build();
        } else {
            zzsClient.submitResponses(id, responses);
            return ResponseEntity.ok().build();
        }
    }

    public ResponseEntity<SurveyDto> getSurveyById(String id) {
        return zzsClient.getSurvey(id);
    }
}
