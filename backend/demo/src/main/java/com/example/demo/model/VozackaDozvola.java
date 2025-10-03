package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "driving_licenses")
public class VozackaDozvola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate dateOfIssuing;
    private LocalDate validUntil;
    private String city;

    @Column(unique = true, nullable = false)
    private String licenseNumber;

    @ElementCollection()
    private List<String> categories;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Korisnik user;

    @Enumerated(EnumType.STRING)
    private StatusZahteva status;

    public static VozackaDozvola kreiraj(Korisnik korisnik, String grad, List<String> kategorije) {
        LocalDate danas = LocalDate.now();

        return VozackaDozvola.builder()
                .firstName(korisnik.getFirstName())
                .lastName(korisnik.getLastName())
                .dateOfBirth(korisnik.getDateOfBirth())
                .city(grad)
                .dateOfIssuing(danas)
                .validUntil(danas.plusYears(10))
                .licenseNumber(UUID.randomUUID().toString())
                .categories(kategorije)
                .user(korisnik)
                .status(StatusZahteva.CEKANJE)
                .build();
    }

}
