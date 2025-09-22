package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private Long id;
    private String ime;
    private String prezime;
    private String email;
    private String grad;
    private String adresa;
    private String jmbg;
    private String pol;

}
