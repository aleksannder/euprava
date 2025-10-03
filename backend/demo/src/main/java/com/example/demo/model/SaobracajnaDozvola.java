package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vehicle_licences")
public class SaobracajnaDozvola extends VrstaZahteva {

    private String make;
    private String model;
    private int displacement;
    private int manufacturedYear;
    private String fuelSystem;
    private String plateNumber;

    private LocalDate dateOfIssuing;
    private LocalDate validUntil;

    private String firstName;
    private String lastName;
    private String address;

    @Column(unique = true, nullable = false)
    private String drivingLicenseNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Korisnik user;

    @Enumerated(EnumType.STRING)
    private StatusZahteva status;

    public static SaobracajnaDozvola kreiraj(Korisnik korisnik,
                                             String marka,
                                             String model,
                                             int kubikaza,
                                             int godiste,
                                             String vrstaPogona,
                                             String tablice,
                                             String adresa) {
        LocalDate danas = LocalDate.now();
        return SaobracajnaDozvola.builder()
                .firstName(korisnik.getFirstName())
                .lastName(korisnik.getLastName())
                .address(adresa)
                .make(marka)
                .model(model)
                .displacement(kubikaza)
                .manufacturedYear(godiste)
                .fuelSystem(vrstaPogona)
                .plateNumber(tablice)
                .dateOfIssuing(danas)
                .validUntil(danas.plusYears(5))
                .drivingLicenseNumber(UUID.randomUUID().toString())
                .user(korisnik)
                .status(StatusZahteva.CEKANJE)
                .build();
    }
}

