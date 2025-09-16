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
    private final JwtTokenService jwt;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtTokenService jwt, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwt = jwt;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRegisterResponseDto> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        User u = userService.registerUser(userRegisterRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserRegisterResponseDto.of(u));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequestDto.email().toLowerCase(), userLoginRequestDto.password()));

        User user = userService.findByEmail(userLoginRequestDto.email().toLowerCase());
        if (user == null) {
            throw new BadCredentialsException("User not found");
        }
        if (!user.getEnabled()) throw new BadCredentialsException("User is inactive");

        Set<String> roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());

        String access = jwt.generateAccessToken(user.getEmail(), roles);
        String refresh = jwt.generateRefreshToken(user.getEmail());

        return ResponseEntity.ok(new UserLoginResponseDto(
                "Bearer", access, jwt.accessTtl(), refresh, jwt.refreshTtl(), user.getEmail(), roles
        ));
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<UserLoginResponseDto> refresh(@Valid @RequestBody RefreshRequestDto refreshRequestDto) {
        if (!jwt.isRefresh(refreshRequestDto.refreshToken())) {
            throw new BadCredentialsException("Refresh token is invalid");
        }
        var claims = jwt.parse(refreshRequestDto.refreshToken()).getBody();
        String email = claims.getSubject();

        User user = userService.findByEmail(email.toLowerCase());
        if (user == null) {
            throw new BadCredentialsException("User not found");
        }
        if (!user.getEnabled()) throw new BadCredentialsException("User is inactive");

        Set<String> roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        String newAccess = jwt.generateAccessToken(user.getEmail(), roles);
        String newRefresh = jwt.generateRefreshToken(user.getEmail());

        return ResponseEntity.ok(new UserLoginResponseDto(
                "Bearer", newAccess, jwt.accessTtl(),
                newRefresh, jwt.refreshTtl(), user.getEmail(), roles
        ));
    }
}
