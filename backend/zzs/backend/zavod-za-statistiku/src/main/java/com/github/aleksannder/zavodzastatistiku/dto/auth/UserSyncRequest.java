package com.github.aleksannder.zavodzastatistiku.dto.auth;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.enums.Role;

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
