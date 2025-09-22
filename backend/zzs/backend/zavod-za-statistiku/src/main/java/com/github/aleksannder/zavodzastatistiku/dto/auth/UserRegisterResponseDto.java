package com.github.aleksannder.zavodzastatistiku.dto.auth;

import com.github.aleksannder.zavodzastatistiku.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
public record UserRegisterResponseDto(String id, String email) {}
