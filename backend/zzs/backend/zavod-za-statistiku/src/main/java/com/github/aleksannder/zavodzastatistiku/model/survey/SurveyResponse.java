package com.github.aleksannder.zavodzastatistiku.model.survey;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "survey_response")
public class SurveyResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // vezano za citizen user-a (možemo kasnije povezati sa User entitetom)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private SurveyQuestion question;

    private String answer; // slobodan tekst ili value za izbor

    private LocalDateTime submittedAt = LocalDateTime.now();
}
