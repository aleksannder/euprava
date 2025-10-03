package com.example.demo.model;

import lombok.Getter;

@Getter
public enum Region {
    RS11_BELGRADE("Beogradski region"),
    RS12_VOJVODINA("Region Vojvodine"),
    RS21_WEST_SUMADIJA("Region Šumadije i Zapadne Srbije"),
    RS22_SOUTH_EAST("Region Južne i Istočne Srbije"),
    RS23_KOSOVO_AND_METOHIJA("Kosovo i Metohija");

    private final String label;

    Region(String label) {
        this.label = label;
    }

}
