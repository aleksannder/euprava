package com.github.aleksannder.zavodzastatistiku.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "import_job")
public class ImportJob {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Dataset dataset;

    @Column(nullable = false)
    private String sourceType;

    @Column(nullable = false)
    private String status;

    private String errorReportPath;

    @CreationTimestamp
    private Instant startedAt;

    private Instant finishedAt;
}
