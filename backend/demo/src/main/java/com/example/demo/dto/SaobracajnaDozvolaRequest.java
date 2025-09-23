package com.example.demo.dto;

import lombok.Data;

@Data
public class SaobracajnaDozvolaRequest {
    private String marka;
    private String model;
    private int kubikaza;
    private int godiste;
    private String vrstaPogona;
    private String tablice;
}
