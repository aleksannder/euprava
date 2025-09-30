package com.github.aleksannder.zavodzastatistiku.model.survey;

import com.github.aleksannder.zavodzastatistiku.model.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "survey_question")
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionType type; // TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE, NUMBER

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;
}
