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
@Table(name = "data_point", indexes = {
        @Index(name = "idx_data_point_indicator", columnList = "indicator_id"),
        @Index(name = "idx_data_point_dataset_version", columnList = "dataset_version_id")
})
public class DataPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "indicator_id")
    private Indicator indicator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dataset_version_id")
    private DatasetVersion datasetVersion;

    @Lob
    @Column(columnDefinition = "jsonb", nullable = false)
    private String dims;

    @Lob
    @Column(columnDefinition = "jsonb", nullable = false)
    private String measures;

    @CreationTimestamp
    private Instant createdAt;
}
