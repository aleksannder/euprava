package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.config.security.JwtTokenService;
import com.github.aleksannder.zavodzastatistiku.dto.auth.*;
import com.github.aleksannder.zavodzastatistiku.model.User;
import com.github.aleksannder.zavodzastatistiku.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRegisterResponseDto> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        User u = userService.registerUser(userRegisterRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserRegisterResponseDto.of(u));
    }

}
