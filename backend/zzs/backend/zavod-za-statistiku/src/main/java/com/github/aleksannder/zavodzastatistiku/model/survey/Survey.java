package com.github.aleksannder.zavodzastatistiku.model.survey;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "survey")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private LocalDateTime createdAt =  LocalDateTime.now();

    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    private Region targetRegion;

    private boolean active = true;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestion> questions;
}
