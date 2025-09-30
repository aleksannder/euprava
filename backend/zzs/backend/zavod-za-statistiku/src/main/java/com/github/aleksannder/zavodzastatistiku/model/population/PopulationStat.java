package com.github.aleksannder.zavodzastatistiku.model.population;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="population_stat")
public class PopulationStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Region region;


    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Long population;

    @Column
    private Double averageAge;

    @Column
    private Double birthRate;

    @Column
    private Double mortalityRate;
}
