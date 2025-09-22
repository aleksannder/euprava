package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oruzje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;

    private String ime;
    private String prezime;
    private LocalDate datumOd;
    private LocalDate datumDo;
    private String regBroj;
    private String kategorijaOruzja;

    @Enumerated(EnumType.STRING)
    private StatusZahteva status;

    public static Oruzje kreiraj(String ime, String prezime, LocalDate datumOd, LocalDate datumDo, String regBroj, String kategorijaOruzja, Korisnik korisnik) {
        return Oruzje.builder()
                .ime(ime)
                .prezime(prezime)
                .datumOd(datumOd)
                .datumDo(datumDo)
                .regBroj(regBroj)
                .kategorijaOruzja(kategorijaOruzja)
                .status(StatusZahteva.CEKANJE)
                .korisnik(korisnik)
                .build();
    }

}
