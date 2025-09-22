package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

public class OruzjeRequest {
    private LocalDate datumOd;
    private List<String> kategorijaOruzja;

    public LocalDate getDatumOd() { return datumOd; }
    public void setDatumOd(LocalDate datumOd) { this.datumOd = datumOd; }

    public List<String> getKategorijaOruzja() { return kategorijaOruzja; }
    public void setKategorijaOruzja(List<String> kategorijaOruzja) { this.kategorijaOruzja = kategorijaOruzja; }
}
