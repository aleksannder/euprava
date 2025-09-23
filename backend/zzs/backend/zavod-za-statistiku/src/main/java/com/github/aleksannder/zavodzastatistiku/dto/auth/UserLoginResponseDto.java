package com.github.aleksannder.zavodzastatistiku.dto.auth;

import java.util.Set;


public record UserLoginResponseDto (
        String accessToken,
        long expiresIn,
        String refreshToken,
        long refreshExpiresIn,
        String tokenType,
        String sessionState,
        String scope
) {}