package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zahtev {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer zahtevID;

    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date datumKreiranja = new Date();

    @Enumerated(EnumType.STRING)
    private StatusZahteva status;
    private String razlogOdbijanja;
    private String kategorijaOruzja;

    @ManyToOne
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;

    @ManyToOne
    @JoinColumn(name = "vrsta_zahteva_id")
    private VrstaZahteva vrstaZahteva;

    public void promeniStatus(StatusZahteva noviStatus) {
        this.status = noviStatus;
    }

}
