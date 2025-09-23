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
public class SaobracajnaDozvola extends VrstaZahteva {

    private String marka;
    private String model;
    private int kubikaza;
    private int godiste;
    private String vrstaPogona;
    private String tablice;

    private LocalDate datumIzdavanja;
    private LocalDate datumVazenja;

    private String ime;
    private String prezime;
    private String adresa;

    @Column(unique = true, nullable = false)
    private String brojDozvole;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

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
                .ime(korisnik.getIme())
                .prezime(korisnik.getPrezime())
                .adresa(adresa)
                .marka(marka)
                .model(model)
                .kubikaza(kubikaza)
                .godiste(godiste)
                .vrstaPogona(vrstaPogona)
                .tablice(tablice)
                .datumIzdavanja(danas)
                .datumVazenja(danas.plusYears(5))
                .brojDozvole(UUID.randomUUID().toString())
                .korisnik(korisnik)
                .status(StatusZahteva.CEKANJE)
                .build();
    }
}

