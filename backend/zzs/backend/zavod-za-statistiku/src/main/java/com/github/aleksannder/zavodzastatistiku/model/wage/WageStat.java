package com.github.aleksannder.zavodzastatistiku.model.wage;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "wage_stat")
public class WageStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Region region;

    private int year;

    private Double averageWage;

    private Double growthPercent;
}