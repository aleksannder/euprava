package com.github.aleksannder.zavodzastatistiku.dto.auth;

import java.util.Set;


public record UserLoginResponseDto (
        String tokenType,
        String accessToken,
        long expiresIn,
        String refreshToken,
        long refreshExpiresIn,
        String email,
        Set<String> roles
) {}