package com.github.aleksannder.zavodzastatistiku.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "measure", uniqueConstraints = @UniqueConstraint(columnNames = {"indicator_id", "name"}))
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "indicator_id")
    private Indicator indicator;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 8)
    private String agg;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;
}
