package com.github.aleksannder.zavodzastatistiku.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Map;

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

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> dims;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> measures;

    @CreationTimestamp
    private Instant createdAt;
}
