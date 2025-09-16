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
@Table(name = "dataset_version")
public class DatasetVersion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Dataset dataset;

    @Column(nullable = false)
    private String version; // semver

    @Lob
    private String schemaJson;

    private String checksum;

    private long rowCount;

    @CreationTimestamp
    private Instant createdAt;

    private Instant publishedAt;

    private String publishedBy;
}
