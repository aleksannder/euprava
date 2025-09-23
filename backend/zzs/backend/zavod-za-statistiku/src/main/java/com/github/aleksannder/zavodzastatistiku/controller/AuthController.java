package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.auth.*;
import com.github.aleksannder.zavodzastatistiku.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequestDto registerRequest) {
        String auth0Id = userService.registerAndLinkToAuth0(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("auth0UserId", auth0Id,
                "success", true));
    }

}
