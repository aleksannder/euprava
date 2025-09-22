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
public class LicnaKarta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ime;
    private String prezime;
    private LocalDate datumRodjenja;
    private String pol;
    private String jmbg;

    @Column(unique = true, nullable = false)
    private String regBroj;

    private LocalDate datumIzdavanja;
    private LocalDate datumVazenja;

    private String grad;
    private String drzava;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

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
                .ime(korisnik.getIme())
                .prezime(korisnik.getPrezime())
                .datumRodjenja(datumRodjenja)
                .pol(pol)
                .jmbg(jmbg)
                .grad(grad)
                .drzava(drzava)
                .datumIzdavanja(danas)
                .datumVazenja(danas.plusYears(10))
                .regBroj(UUID.randomUUID().toString())
                .korisnik(korisnik)
                .status(StatusZahteva.CEKANJE)
                .build();
    }

}
