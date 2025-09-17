package com.github.aleksannder.zavodzastatistiku.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import com.github.aleksannder.zavodzastatistiku.model.enums.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/citizen")
public class CitizenController {

    @GetMapping("/ping")
    @PreAuthorize("hasAnyRole('CITIZEN', 'ANALYST', 'ADMIN')")
    public Map<String, Object> ping(Authentication auth) {
        return Map.of(
                "ok", true,
                "user", auth.getName(),
                "role", auth.getAuthorities()
        );
    }
}
