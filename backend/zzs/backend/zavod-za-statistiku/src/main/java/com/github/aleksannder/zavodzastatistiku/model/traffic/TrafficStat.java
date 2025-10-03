package com.github.aleksannder.zavodzastatistiku.model.traffic;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "traffic_stat")
public class TrafficStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Region region;

    private int year;

    private Long registeredVehicles;

    private Long trafficAccidents;

    private Long fatalities;
}
