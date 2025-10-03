package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "requests")
public class Zahtev {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private StatusZahteva status;
    private String denial_reason;
    private String gun_category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Korisnik user;

    @ManyToOne
    @JoinColumn(name = "request_type_id")
    private VrstaZahteva requestType;

    public void promeniStatus(StatusZahteva noviStatus) {
        this.status = noviStatus;
    }

}
