package com.github.aleksannder.zavodzastatistiku.model.gdp;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "gdp_stat")
public class GdpStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Region region;

    private int year;

    private Double gdpBillion;

    private Double growthPercent;

    private Double cpiPercent;

}
