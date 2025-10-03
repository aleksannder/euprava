package com.github.aleksannder.zavodzastatistiku.model;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String city;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private Region region;

    private String jmbg;

    private String gender;

    @CreationTimestamp
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="auth0_user_id", nullable = false)
    private String auth0UserId;
}
