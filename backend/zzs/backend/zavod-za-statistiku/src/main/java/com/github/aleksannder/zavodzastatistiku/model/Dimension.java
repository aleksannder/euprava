package com.github.aleksannder.zavodzastatistiku.model;

import com.github.aleksannder.zavodzastatistiku.model.enums.DimensionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "dimension", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type"}))
public class Dimension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private DimensionType type;

    @Lob
    @Column(columnDefinition = "jsonb")
    private String allowedValues;
}
