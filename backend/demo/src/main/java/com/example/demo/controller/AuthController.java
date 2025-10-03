package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Korisnik;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.service.KorisnikService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KorisnikService korisnikService;
    private final KorisnikRepository korisnikRepository;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String response = korisnikService.registerAndLinkToAuth0(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "auth0UserId", response,
                    "success", true
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/me/{authHeader}")
    public ResponseEntity<?> getCurrentUser(@PathVariable String authHeader) {
        try {
            Korisnik korisnik = korisnikRepository.findByEmail(authHeader)
                    .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

            return ResponseEntity.ok(new UserProfileResponse(
                    korisnik.getId(),
                    korisnik.getFirstName(),
                    korisnik.getLastName(),
                    korisnik.getEmail(),
                    korisnik.getRole()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/update/mup")
    public void sendUserDataToMupService(@RequestBody UserSyncRequest korisnik) {
        korisnikService.updateDbWithUserFromStatisticsService(korisnik);
    }
}
