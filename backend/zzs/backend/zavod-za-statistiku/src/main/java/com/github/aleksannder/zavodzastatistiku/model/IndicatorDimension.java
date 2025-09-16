package com.github.aleksannder.zavodzastatistiku.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "indicator_dimension")
public class IndicatorDimension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "indicator_id")
    private Indicator indicator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dimension_id")
    private Dimension dimension;

    @Column(nullable = false)
    private Integer ord;
}
