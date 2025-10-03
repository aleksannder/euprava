package com.example.demo.dto;

import com.example.demo.model.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String ime;
    private String prezime;
    private String email;
    private String lozinka;
    private LocalDate datumRodjenja;
    private String grad;
    private Region region;
    private String adresa;
    private String pol;

}
