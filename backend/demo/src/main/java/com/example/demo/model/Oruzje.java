package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "gun_permits")
public class Oruzje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Korisnik user;

    private String firstName;
    private String lastName;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String registrationNumber;
    private String gunCategory;

    @Enumerated(EnumType.STRING)
    private StatusZahteva status;

    public static Oruzje kreiraj(String ime, String prezime, LocalDate datumOd, LocalDate datumDo, String regBroj, String kategorijaOruzja, Korisnik korisnik) {
        return Oruzje.builder()
                .firstName(ime)
                .lastName(prezime)
                .dateFrom(datumOd)
                .dateTo(datumDo)
                .registrationNumber(regBroj)
                .gunCategory(kategorijaOruzja)
                .status(StatusZahteva.CEKANJE)
                .user(korisnik)
                .build();
    }

}
