package com.github.aleksannder.zavodzastatistiku.dto.auth;

import lombok.Builder;

@Builder
public record UserRegisterResponseDto(String id, String email) {}
