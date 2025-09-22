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
public class VozackaDozvola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ime;
    private String prezime;
    private LocalDate datumRodjenja;
    private LocalDate datumIzdavanja;
    private LocalDate datumVazenja;
    private String grad;

    @Column(unique = true, nullable = false)
    private String brojDozvole;

    @ElementCollection
    private List<String> kategorije;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

    @Enumerated(EnumType.STRING)
    private StatusZahteva status;

    public static VozackaDozvola kreiraj(Korisnik korisnik, String grad, List<String> kategorije) {
        LocalDate danas = LocalDate.now();

        return VozackaDozvola.builder()
                .ime(korisnik.getIme())
                .prezime(korisnik.getPrezime())
                .datumRodjenja(korisnik.getDatumRodjenja())
                .grad(grad)
                .datumIzdavanja(danas)
                .datumVazenja(danas.plusYears(10))
                .brojDozvole(UUID.randomUUID().toString())
                .kategorije(kategorije)
                .korisnik(korisnik)
                .status(StatusZahteva.CEKANJE)
                .build();
    }

}
