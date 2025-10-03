package com.example.demo.dto;

import com.example.demo.model.Region;
import com.example.demo.model.Role;

import java.time.Instant;
import java.time.LocalDate;

public record UserSyncRequest(
    String email,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String city,
    String address,
    Region region,
    String jmbg,
    String gender,
    Instant createdAt,
    Role role,
    String auth0UserId
) {
}
