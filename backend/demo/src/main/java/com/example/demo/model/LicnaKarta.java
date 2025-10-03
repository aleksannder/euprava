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
@Table(name = "identity_cards")
public class LicnaKarta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String gender;
    private String jmbg;

    @Column(unique = true, nullable = false)
    private String registrationNumber;

    private LocalDate dateOfIssuing;
    private LocalDate validUntil;

    private String city;
    private String state;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Korisnik user;

    @Enumerated(EnumType.STRING)
    private StatusZahteva status;

    public static LicnaKarta kreiraj(Korisnik korisnik,
                                     String jmbg,
                                     LocalDate datumRodjenja,
                                     String pol,
                                     String grad,
                                     String drzava) {
        LocalDate danas = LocalDate.now();

        return LicnaKarta.builder()
                .name(korisnik.getFirstName())
                .surname(korisnik.getLastName())
                .dateOfBirth(datumRodjenja)
                .gender(pol)
                .jmbg(jmbg)
                .city(grad)
                .state(drzava)
                .dateOfIssuing(danas)
                .validUntil(danas.plusYears(10))
                .registrationNumber(UUID.randomUUID().toString())
                .user(korisnik)
                .status(StatusZahteva.CEKANJE)
                .build();
    }

}
