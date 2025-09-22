package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Korisnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long korisnikID;

    private String ime;
    private String prezime;
    private String email;
    private String lozinka;
    private LocalDate datumRodjenja;
    private String grad;
    private String adresa;

    private String jmbg;
    private String pol;

    @Enumerated(EnumType.STRING)
    private Role rola;

}
