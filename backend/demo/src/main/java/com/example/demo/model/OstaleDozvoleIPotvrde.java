package com.example.demo.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OstaleDozvoleIPotvrde extends VrstaZahteva {
    public void potvrdaNeKaznjavanosti() {}
    public void izvodMaticneKnjige() {}
    public void dozvolaZaOruzje() {}
    public void produzavanjeDozvoleZaOruzje() {}
}
