package com.github.aleksannder.zavodzastatistiku.dto.auth;

import com.github.aleksannder.zavodzastatistiku.model.User;
import lombok.Data;

import java.util.Set;

public record UserRegisterResponseDto(String id, String email, boolean emailVerified, boolean enabled) {}
