package com.github.aleksannder.zavodzastatistiku.dto.auth;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;
import com.github.aleksannder.zavodzastatistiku.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserRegisterRequestDto(
        @Email String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String city,
        @NotBlank String address,
        @NotBlank String gender,
        @NotNull LocalDate dateOfBirth,
        @NotBlank @Size(min=8, message = "Password must be at least 8 characters") String password,
        @NotNull Role role,
        @NotNull Region region
) {}
