package com.github.aleksannder.zavodzastatistiku.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDto(@NotBlank String refreshToken) {
}
