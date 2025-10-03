package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

public class OruzjeRequest {
    private LocalDate dateFrom;
    private List<String> gunCategories;

    public LocalDate getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDate dateFrom) { this.dateFrom = dateFrom; }

    public List<String> getGunCategories() { return gunCategories; }
    public void setGunCategories(List<String> gunCategories) { this.gunCategories = gunCategories; }
}
