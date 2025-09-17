package com.github.aleksannder.zavodzastatistiku.dto.auth;

import com.github.aleksannder.zavodzastatistiku.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestDto(
        @Email String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Size(min=8, message = "Password must be at least 8 characters") String password,
        @NotNull Role role
) {}
